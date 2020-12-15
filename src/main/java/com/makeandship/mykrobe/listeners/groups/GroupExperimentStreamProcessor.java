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
import com.makeandship.mykrobe.models.GroupExperiment;
import com.makeandship.mykrobe.models.GroupExperimentKey;
import com.makeandship.mykrobe.models.mongo.MongoGroup;
import com.makeandship.mykrobe.models.mongo.MongoId;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ GroupExperimentStreamBinding.class })
public class GroupExperimentStreamProcessor {
	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processGroupExperiments", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<GroupExperimentKey, GroupExperiment>> store = buildGroupExperimentStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo(GroupExperimentStreamBinding.SINK_CORE_GROUP_EXPERIMENTS)
	@StreamListener(GroupExperimentStreamBinding.SOURCE_GROUP_EXPERIMENTS)
	public KStream<GroupExperimentKey, GroupExperiment> processGroupExperiments(KStream<Object, DebeziumGroup> stream) {
		log.info(GroupExperimentStreamProcessor.class.getName() + "#process: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return stream.flatMap(
				new KeyValueMapper<Object, DebeziumGroup, Iterable<KeyValue<GroupExperimentKey, GroupExperiment>>>() {
					@Override
					public Iterable<KeyValue<GroupExperimentKey, GroupExperiment>> apply(Object key,
							DebeziumGroup value) {
						return getExperiments(value);
					}
				}).map((key, value) -> new KeyValue<>(key, value))
				.transform(new GroupExperimentTransformSupplier(Constants.STORE_CORE_GROUP_EXPERIMENT),
						Constants.STORE_CORE_GROUP_EXPERIMENT);
	}

	private Iterable<KeyValue<GroupExperimentKey, GroupExperiment>> getExperiments(DebeziumGroup value) {
		List<KeyValue<GroupExperimentKey, GroupExperiment>> list = new ArrayList<KeyValue<GroupExperimentKey, GroupExperiment>>();

		if (value.getPayload().getAfter() != null) {
			MongoGroup group = value.getPayload().getAfter();
			if (group.getExperiments() != null && group.getExperiments().size() > 0) {
				for (MongoId mongoExperiment : group.getExperiments()) {
					// key
					GroupExperimentKey key = new GroupExperimentKey();
					key.setExperimentId(mongoExperiment.getOid());
					key.setGroupId(group.getId().getOid());

					// value
					GroupExperiment groupExperiment = new GroupExperiment();
					groupExperiment.setExperimentId(mongoExperiment.getOid());
					groupExperiment.setGroupId(group.getId().getOid());
					list.add(new KeyValue<>(key, groupExperiment));
				}
			}

		} else if (value.getPayload().getPatch() != null) {
			MongoGroup group = value.getPayload().getPatch().getPart();
			if (group.getExperiments() != null && group.getExperiments().size() > 0) {
				for (MongoId mongoExperiment : group.getExperiments()) {
					String mongoId = value.getPayload().getFilter().getId().getOid();
					// key
					GroupExperimentKey key = new GroupExperimentKey();
					key.setGroupId(mongoId);
					key.setExperimentId(mongoExperiment.getOid());

					// value
					GroupExperiment groupExperiment = new GroupExperiment();
					groupExperiment.setGroupId(mongoId);
					groupExperiment.setExperimentId(mongoExperiment.getOid());
					list.add(new KeyValue<>(key, groupExperiment));
				}
			}
		}

		return list;
	}

	private StoreBuilder<KeyValueStore<GroupExperimentKey, GroupExperiment>> buildGroupExperimentStore() {
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<GroupExperimentKey> keyAvroSerde = new SpecificAvroSerde<GroupExperimentKey>();
		final Serde<GroupExperiment> valueAvroSerde = new SpecificAvroSerde<GroupExperiment>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.STORE_CORE_GROUP_EXPERIMENT),
				keyAvroSerde, valueAvroSerde);
	}

	private static class GroupExperimentTransformSupplier implements
			TransformerSupplier<GroupExperimentKey, GroupExperiment, KeyValue<GroupExperimentKey, GroupExperiment>> {

		final private String stateStoreName;

		public GroupExperimentTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<GroupExperimentKey, GroupExperiment, KeyValue<GroupExperimentKey, GroupExperiment>> get() {
			return new Transformer<GroupExperimentKey, GroupExperiment, KeyValue<GroupExperimentKey, GroupExperiment>>() {

				private KeyValueStore<GroupExperimentKey, GroupExperiment> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<GroupExperimentKey, GroupExperiment>) processorContext
							.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<GroupExperimentKey, GroupExperiment> transform(GroupExperimentKey key,
						GroupExperiment value) {

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
