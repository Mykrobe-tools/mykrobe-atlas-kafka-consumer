package com.makeandship.mykrobe.listeners.experiment;

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
import com.makeandship.mykrobe.factories.ExperimentTransformersFactory;
import com.makeandship.mykrobe.models.DebeziumExperiment;
import com.makeandship.mykrobe.models.DebeziumExperimentPayload;
import com.makeandship.mykrobe.models.Experiment;
import com.makeandship.mykrobe.models.ExperimentKey;
import com.makeandship.mykrobe.transformers.ExperimentTransformer;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ ExperimentStreamBinding.class })
public class ExperimentStreamProcessor {

	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processCoreExperiments", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<ExperimentKey, Experiment>> store = buildExperimentStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo(ExperimentStreamBinding.SINK_CORE_EXPERIMENTS)
	@StreamListener(ExperimentStreamBinding.SOURCE_EXPERIMENTS)
	public KStream<ExperimentKey, Experiment> processCoreExperiments(KStream<Object, DebeziumExperiment> stream) {
		log.debug(ExperimentStreamProcessor.class.getName() + "#processCoreExperiments: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return stream.transform(new ExperimentTransformSupplier(Constants.STORE_CORE_EXPERIMENT),
				Constants.STORE_CORE_EXPERIMENT);
	}

	private StoreBuilder<KeyValueStore<ExperimentKey, Experiment>> buildExperimentStore() {
		log.debug(ExperimentStreamProcessor.class.getName() + "#buildExperimentStore: enter");
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<ExperimentKey> keyAvroSerde = new SpecificAvroSerde<ExperimentKey>();
		final Serde<Experiment> valueAvroSerde = new SpecificAvroSerde<Experiment>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		log.debug(ExperimentStreamProcessor.class.getName() + "#buildExperimentStore: exit");
		
		return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.STORE_CORE_EXPERIMENT),
				keyAvroSerde, valueAvroSerde);
	}

	private static class ExperimentTransformSupplier
			implements TransformerSupplier<Object, DebeziumExperiment, KeyValue<ExperimentKey, Experiment>> {

		final private String stateStoreName;

		public ExperimentTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<Object, DebeziumExperiment, KeyValue<ExperimentKey, Experiment>> get() {
			return new Transformer<Object, DebeziumExperiment, KeyValue<ExperimentKey, Experiment>>() {

				private KeyValueStore<ExperimentKey, Experiment> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<ExperimentKey, Experiment>) processorContext
							.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<ExperimentKey, Experiment> transform(Object key, DebeziumExperiment value) {
					log.debug(ExperimentTransformSupplier.class.getName() + "#transform: enter");
					
					DebeziumExperimentPayload payload = value.getPayload();
					ExperimentTransformer transformer = ExperimentTransformersFactory.create(stateStore, payload);
  
					if (transformer != null) {
						log.debug(ExperimentTransformSupplier.class.getName() + "#transform: call transform");
						return transformer.transform();
					}

					log.debug(ExperimentTransformSupplier.class.getName() + "#transform: exit (null)");
					
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
