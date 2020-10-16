package com.makeandship.mykrobe.listeners.groups;

import java.util.Collections;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
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
import com.makeandship.mykrobe.factories.GroupTransformersFactory;
import com.makeandship.mykrobe.models.DebeziumGroup;
import com.makeandship.mykrobe.models.DebeziumGroupPayload;
import com.makeandship.mykrobe.models.Group;
import com.makeandship.mykrobe.models.GroupKey;
import com.makeandship.mykrobe.transformers.GroupTransformer;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ GroupStreamBinding.class })
public class GroupStreamProcessor {

	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processCoreGroups", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<GroupKey, Group>> store = buildGroupStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo(GroupStreamBinding.SINK_CORE_GROUPS)
	@StreamListener(GroupStreamBinding.SOURCE_GROUPS)
	public KStream<GroupKey, Group> processCoreGroups(KStream<Object, DebeziumGroup> stream) {
		log.debug(GroupStreamProcessor.class.getName() + "#processCoreGroups: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return stream.transform(new GroupTransformSupplier(Constants.STORE_CORE_GROUP), Constants.STORE_CORE_GROUP);
	}

	private StoreBuilder<KeyValueStore<GroupKey, Group>> buildGroupStore() {
		log.debug(GroupStreamProcessor.class.getName() + "#buildGroupStore: enter");
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<GroupKey> keyAvroSerde = new SpecificAvroSerde<GroupKey>();
		final Serde<Group> valueAvroSerde = new SpecificAvroSerde<Group>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		log.debug(GroupStreamProcessor.class.getName() + "#buildGroupStore: exit");

		return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.STORE_CORE_GROUP), keyAvroSerde,
				valueAvroSerde);
	}

	private static class GroupTransformSupplier
			implements TransformerSupplier<Object, DebeziumGroup, KeyValue<GroupKey, Group>> {

		final private String stateStoreName;

		public GroupTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<Object, DebeziumGroup, KeyValue<GroupKey, Group>> get() {
			return new Transformer<Object, DebeziumGroup, KeyValue<GroupKey, Group>>() {

				private KeyValueStore<GroupKey, Group> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<GroupKey, Group>) processorContext.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<GroupKey, Group> transform(Object key, DebeziumGroup value) {
					log.debug(GroupTransformSupplier.class.getName() + "#transform: enter");

					DebeziumGroupPayload payload = value.getPayload();
					GroupTransformer transformer = GroupTransformersFactory.create(stateStore, payload);

					if (transformer != null) {
						log.debug(GroupTransformSupplier.class.getName() + "#transform: call transform");
						return transformer.transform();
					}

					log.debug(GroupTransformSupplier.class.getName() + "#transform: exit (null)");

					return null;
				}

				@Override
				public void close() {
					// No need to close as this is handled by kafka.
				}
			};
		}
	}
}
