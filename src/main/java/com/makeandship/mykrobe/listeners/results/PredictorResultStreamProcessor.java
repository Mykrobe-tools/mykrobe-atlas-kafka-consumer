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
import com.makeandship.mykrobe.converters.DateConverter;
import com.makeandship.mykrobe.converters.ResultConverter;
import com.makeandship.mykrobe.models.DebeziumExperiment;
import com.makeandship.mykrobe.models.ExperimentResult;
import com.makeandship.mykrobe.models.PredictorResult;
import com.makeandship.mykrobe.models.PredictorResultKey;
import com.makeandship.mykrobe.models.mongo.MongoExperiment;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ PredictorResultStreamBinding.class })
public class PredictorResultStreamProcessor {
	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processPredictorResult", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<PredictorResultKey, PredictorResult>> store = buildPredictorResultStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo(PredictorResultStreamBinding.SINK_CORE_PREDICTOR_RESULT)
	@StreamListener(PredictorResultStreamBinding.SOURCE_PREDICTOR_RESULT)
	public KStream<PredictorResultKey, PredictorResult> processPredictorResult(
			KStream<Object, DebeziumExperiment> stream) {
		log.info(PredictorResultStreamProcessor.class.getName() + "#process: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return stream.flatMap(
				new KeyValueMapper<Object, DebeziumExperiment, Iterable<KeyValue<PredictorResultKey, PredictorResult>>>() {
					@Override
					public Iterable<KeyValue<PredictorResultKey, PredictorResult>> apply(Object key,
							DebeziumExperiment value) {
						return getResults(value);
					}
				}).map((key, value) -> new KeyValue<>(key, value))
				.transform(new PredictorResultTransformSupplier(Constants.STORE_CORE_PREDICTOR_RESULT),
						Constants.STORE_CORE_PREDICTOR_RESULT);
	}

	private Iterable<KeyValue<PredictorResultKey, PredictorResult>> getResults(DebeziumExperiment value) {
		List<KeyValue<PredictorResultKey, PredictorResult>> list = new ArrayList<KeyValue<PredictorResultKey, PredictorResult>>();

		if (value.getPayload().getAfter() != null) {
			MongoExperiment experiment = value.getPayload().getAfter();
			if (experiment.getResults() != null) {
				for (ExperimentResult result : experiment.getResults()) {
					if (Constants.PREDICTOR_RESULT_TYPE.equals(result.getType())) {
						// key
						PredictorResultKey key = new PredictorResultKey();
						key.setExperimentId(experiment.getId().getOid());
						key.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));

						// value
						PredictorResult predictorResult = ResultConverter.toPredictorResult(experiment.getId().getOid(),
								result);
						list.add(new KeyValue<>(key, predictorResult));
					}
				}
			}

		} else if (value.getPayload().getPatch() != null) {
			MongoExperiment experiment = value.getPayload().getPatch().getPart();
			if (experiment.getResults() != null) {
				for (ExperimentResult result : experiment.getResults()) {
					if (Constants.PREDICTOR_RESULT_TYPE.equals(result.getType())) {
						String mongoId = value.getPayload().getFilter().getId().getOid();
						// key
						PredictorResultKey key = new PredictorResultKey();
						key.setExperimentId(mongoId);
						key.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));

						// value
						PredictorResult predictorResult = ResultConverter.toPredictorResult(mongoId, result);
						list.add(new KeyValue<>(key, predictorResult));
					}
				}
			}
		}

		return list;
	}

	private StoreBuilder<KeyValueStore<PredictorResultKey, PredictorResult>> buildPredictorResultStore() {
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<PredictorResultKey> keyAvroSerde = new SpecificAvroSerde<PredictorResultKey>();
		final Serde<PredictorResult> valueAvroSerde = new SpecificAvroSerde<PredictorResult>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.STORE_CORE_PREDICTOR_RESULT),
				keyAvroSerde, valueAvroSerde);
	}

	private static class PredictorResultTransformSupplier implements
			TransformerSupplier<PredictorResultKey, PredictorResult, KeyValue<PredictorResultKey, PredictorResult>> {

		final private String stateStoreName;

		public PredictorResultTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<PredictorResultKey, PredictorResult, KeyValue<PredictorResultKey, PredictorResult>> get() {
			return new Transformer<PredictorResultKey, PredictorResult, KeyValue<PredictorResultKey, PredictorResult>>() {

				private KeyValueStore<PredictorResultKey, PredictorResult> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<PredictorResultKey, PredictorResult>) processorContext
							.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<PredictorResultKey, PredictorResult> transform(PredictorResultKey key,
						PredictorResult value) {

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
