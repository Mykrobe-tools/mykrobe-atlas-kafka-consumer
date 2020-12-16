package com.makeandship.mykrobe.listeners.groups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.makeandship.mykrobe.Constants;
import com.makeandship.mykrobe.models.DebeziumGroup;
import com.makeandship.mykrobe.models.GroupSearch;
import com.makeandship.mykrobe.models.GroupSearchKey;
import com.makeandship.mykrobe.models.mongo.MongoGroup;
import com.makeandship.mykrobe.models.mongo.MongoId;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ GroupSearchStreamBinding.class })
public class GroupSearchStreamProcessor {
	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processGroupSearchs", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<GroupSearchKey, GroupSearch>> store = buildGroupSearchStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo(GroupSearchStreamBinding.SINK_CORE_GROUP_SEARCHES)
	@StreamListener(GroupSearchStreamBinding.SOURCE_GROUP_SEARCHES)
	public KStream<GroupSearchKey, GroupSearch> processGroupSearchs(KStream<Object, DebeziumGroup> stream) {
		log.info(GroupSearchStreamProcessor.class.getName() + "#process: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return stream
				.flatMap(new KeyValueMapper<Object, DebeziumGroup, Iterable<KeyValue<GroupSearchKey, GroupSearch>>>() {
					@Override
					public Iterable<KeyValue<GroupSearchKey, GroupSearch>> apply(Object key, DebeziumGroup value) {
						return getSearchs(value);
					}
				}).map((key, value) -> new KeyValue<>(key, value))
				.transform(new GroupSearchTransformSupplier(Constants.STORE_CORE_GROUP_EXPERIMENT),
						Constants.STORE_CORE_GROUP_EXPERIMENT);
	}

	private Iterable<KeyValue<GroupSearchKey, GroupSearch>> getSearchs(DebeziumGroup value) {
		List<KeyValue<GroupSearchKey, GroupSearch>> list = new ArrayList<KeyValue<GroupSearchKey, GroupSearch>>();

		if (value.getPayload().getAfter() != null) {
			MongoGroup group = value.getPayload().getAfter();
			if (group.getSearches() != null && group.getSearches().size() > 0) {
				for (MongoId mongoSearch : group.getSearches()) {
					// key
					GroupSearchKey key = new GroupSearchKey();
					key.setSearchId(mongoSearch.getOid());
					key.setGroupId(group.getId().getOid());

					// value
					GroupSearch groupSearch = new GroupSearch();
					groupSearch.setSearchId(mongoSearch.getOid());
					groupSearch.setGroupId(group.getId().getOid());
					list.add(new KeyValue<>(key, groupSearch));
				}
			}

		} else if (value.getPayload().getPatch() != null) {
			MongoGroup group = value.getPayload().getPatch().getPart();
			if (group.getSearches() != null && group.getSearches().size() > 0) {
				for (MongoId mongoSearch : group.getSearches()) {
					String mongoId = value.getPayload().getFilter().getId().getOid();
					// key
					GroupSearchKey key = new GroupSearchKey();
					key.setGroupId(mongoId);
					key.setSearchId(mongoSearch.getOid());

					// value
					GroupSearch groupSearch = new GroupSearch();
					groupSearch.setGroupId(mongoId);
					groupSearch.setSearchId(mongoSearch.getOid());
					list.add(new KeyValue<>(key, groupSearch));
				}
			}
		}

		return list;
	}

	private StoreBuilder<KeyValueStore<GroupSearchKey, GroupSearch>> buildGroupSearchStore() {
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<GroupSearchKey> keyAvroSerde = new SpecificAvroSerde<GroupSearchKey>();
		final Serde<GroupSearch> valueAvroSerde = new SpecificAvroSerde<GroupSearch>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.STORE_CORE_GROUP_EXPERIMENT),
				keyAvroSerde, valueAvroSerde);
	}

	private static class GroupSearchTransformSupplier
			implements TransformerSupplier<GroupSearchKey, GroupSearch, KeyValue<GroupSearchKey, GroupSearch>> {

		final private String stateStoreName;

		public GroupSearchTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<GroupSearchKey, GroupSearch, KeyValue<GroupSearchKey, GroupSearch>> get() {
			return new Transformer<GroupSearchKey, GroupSearch, KeyValue<GroupSearchKey, GroupSearch>>() {

				private KeyValueStore<GroupSearchKey, GroupSearch> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<GroupSearchKey, GroupSearch>) processorContext
							.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<GroupSearchKey, GroupSearch> transform(GroupSearchKey key, GroupSearch value) {

					// update the store
					stateStore.put(key, value);

					return KeyValue.pair(key, value);
				}

				@Override
				public void close() {
					// No need to close as this is handled by kafka.
				}
			};
		}
	}

}
