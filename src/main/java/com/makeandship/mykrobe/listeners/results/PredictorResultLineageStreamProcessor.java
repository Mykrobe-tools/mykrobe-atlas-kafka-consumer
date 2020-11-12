package com.makeandship.mykrobe.listeners.results;

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
import com.makeandship.mykrobe.models.DebeziumExperiment;
import com.makeandship.mykrobe.models.ExperimentResult;
import com.makeandship.mykrobe.models.MykrobePhylogenetics;
import com.makeandship.mykrobe.models.PredictorResultLineage;
import com.makeandship.mykrobe.models.PredictorResultLineageKey;
import com.makeandship.mykrobe.models.mongo.MongoExperiment;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ PredictorResultLineageStreamBinding.class })
public class PredictorResultLineageStreamProcessor {
	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processPredictorResultLineage", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<PredictorResultLineageKey, PredictorResultLineage>> store = buildPredictorResultLineageStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo(PredictorResultLineageStreamBinding.SINK_CORE_PREDICTOR_RESULT_LINEAGE)
	@StreamListener(PredictorResultLineageStreamBinding.SOURCE_PREDICTOR_RESULT_LINEAGE)
	public KStream<PredictorResultLineageKey, PredictorResultLineage> processPredictorResultLineage(
			KStream<Object, DebeziumExperiment> stream) {
		log.info(PredictorResultLineageStreamProcessor.class.getName() + "#process: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return stream.flatMap(
				new KeyValueMapper<Object, DebeziumExperiment, Iterable<KeyValue<PredictorResultLineageKey, PredictorResultLineage>>>() {
					@Override
					public Iterable<KeyValue<PredictorResultLineageKey, PredictorResultLineage>> apply(Object key,
							DebeziumExperiment value) {
						return getResults(value);
					}
				}).map((key, value) -> new KeyValue<>(key, value))
				.transform(new PredictorResultLineageTransformSupplier(Constants.STORE_CORE_PREDICTOR_RESULT_LINEAGE),
						Constants.STORE_CORE_PREDICTOR_RESULT_LINEAGE);
	}

	private Iterable<KeyValue<PredictorResultLineageKey, PredictorResultLineage>> getResults(DebeziumExperiment value) {
		List<KeyValue<PredictorResultLineageKey, PredictorResultLineage>> list = new ArrayList<KeyValue<PredictorResultLineageKey, PredictorResultLineage>>();

		if (value.getPayload().getAfter() != null) {
			MongoExperiment experiment = value.getPayload().getAfter();
			if (experiment.getResults() != null) {
				for (ExperimentResult result : experiment.getResults()) {
					if (Constants.PREDICTOR_RESULT_TYPE.equals(result.getType())) {
						List<MykrobePhylogenetics> phylogenetics = result.getPhylogenetics();
						if (phylogenetics != null) {
							int index = 0;
							for (MykrobePhylogenetics phylogenetic : phylogenetics) {
								List<String> lineages = phylogenetic.getLineages();
								if (lineages != null) {
									for (String lineage : lineages) {
										// key
										PredictorResultLineageKey key = new PredictorResultLineageKey();
										key.setExperimentId(experiment.getId().getOid());
										key.setIndex(index);

										// value
										PredictorResultLineage predictorResultLineage = new PredictorResultLineage();
										predictorResultLineage.setExperimentId(experiment.getId().getOid());
										predictorResultLineage.setLineage(lineage);
										predictorResultLineage.setIndex(index);
										list.add(new KeyValue<>(key, predictorResultLineage));
										index++;
									}
								}
							}
						}
					}
				}
			}

		} else if (value.getPayload().getPatch() != null) {
			MongoExperiment experiment = value.getPayload().getPatch().getPart();
			if (experiment.getResults() != null) {
				for (ExperimentResult result : experiment.getResults()) {
					if (Constants.PREDICTOR_RESULT_TYPE.equals(result.getType())) {
						String mongoId = value.getPayload().getFilter().getId().getOid();
						List<MykrobePhylogenetics> phylogenetics = result.getPhylogenetics();
						if (phylogenetics != null) {
							int index = 0;
							for (MykrobePhylogenetics phylogenetic : phylogenetics) {
								List<String> lineages = phylogenetic.getLineages();
								if (lineages != null) {
									for (String lineage : lineages) {
										// key
										PredictorResultLineageKey key = new PredictorResultLineageKey();
										key.setExperimentId(mongoId);
										key.setIndex(index);

										// value
										PredictorResultLineage predictorResultLineage = new PredictorResultLineage();
										predictorResultLineage.setExperimentId(mongoId);
										predictorResultLineage.setIndex(index);
										predictorResultLineage.setLineage(lineage);

										list.add(new KeyValue<>(key, predictorResultLineage));
									}
								}
							}
						}

					}
				}
			}
		}

		return list;
	}

	private StoreBuilder<KeyValueStore<PredictorResultLineageKey, PredictorResultLineage>> buildPredictorResultLineageStore() {
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<PredictorResultLineageKey> keyAvroSerde = new SpecificAvroSerde<PredictorResultLineageKey>();
		final Serde<PredictorResultLineage> valueAvroSerde = new SpecificAvroSerde<PredictorResultLineage>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		return Stores.keyValueStoreBuilder(
				Stores.persistentKeyValueStore(Constants.STORE_CORE_PREDICTOR_RESULT_LINEAGE), keyAvroSerde,
				valueAvroSerde);
	}

	private static class PredictorResultLineageTransformSupplier implements
			TransformerSupplier<PredictorResultLineageKey, PredictorResultLineage, KeyValue<PredictorResultLineageKey, PredictorResultLineage>> {

		final private String stateStoreName;

		public PredictorResultLineageTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<PredictorResultLineageKey, PredictorResultLineage, KeyValue<PredictorResultLineageKey, PredictorResultLineage>> get() {
			return new Transformer<PredictorResultLineageKey, PredictorResultLineage, KeyValue<PredictorResultLineageKey, PredictorResultLineage>>() {

				private KeyValueStore<PredictorResultLineageKey, PredictorResultLineage> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<PredictorResultLineageKey, PredictorResultLineage>) processorContext
							.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<PredictorResultLineageKey, PredictorResultLineage> transform(
						PredictorResultLineageKey key, PredictorResultLineage value) {

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
