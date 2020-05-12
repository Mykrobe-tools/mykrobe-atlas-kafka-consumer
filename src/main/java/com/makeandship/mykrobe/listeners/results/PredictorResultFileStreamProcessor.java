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
import com.makeandship.mykrobe.models.DebeziumExperiment;
import com.makeandship.mykrobe.models.ExperimentResult;
import com.makeandship.mykrobe.models.PredictorResultFile;
import com.makeandship.mykrobe.models.PredictorResultFileKey;
import com.makeandship.mykrobe.models.mongo.MongoExperiment;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ PredictorResultFileStreamBinding.class })
public class PredictorResultFileStreamProcessor {
	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processPredictorResultFile", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<PredictorResultFileKey, PredictorResultFile>> store = buildPredictorResultFileStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo(PredictorResultFileStreamBinding.SINK_CORE_PREDICTOR_RESULT_FILE)
	@StreamListener(PredictorResultFileStreamBinding.SOURCE_PREDICTOR_RESULT_FILE)
	public KStream<PredictorResultFileKey, PredictorResultFile> processPredictorResultFile(
			KStream<Object, DebeziumExperiment> stream) {
		log.info(PredictorResultFileStreamProcessor.class.getName() + "#process: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return stream.flatMap(
				new KeyValueMapper<Object, DebeziumExperiment, Iterable<KeyValue<PredictorResultFileKey, PredictorResultFile>>>() {
					@Override
					public Iterable<KeyValue<PredictorResultFileKey, PredictorResultFile>> apply(Object key,
							DebeziumExperiment value) {
						return getResults(value);
					}
				}).map((key, value) -> new KeyValue<>(key, value))
				.transform(new PredictorResultFileTransformSupplier(Constants.STORE_CORE_PREDICTOR_RESULT_FILE),
						Constants.STORE_CORE_PREDICTOR_RESULT_FILE);
	}

	private Iterable<KeyValue<PredictorResultFileKey, PredictorResultFile>> getResults(DebeziumExperiment value) {
		List<KeyValue<PredictorResultFileKey, PredictorResultFile>> list = new ArrayList<KeyValue<PredictorResultFileKey, PredictorResultFile>>();

		if (value.getPayload().getAfter() != null) {
			MongoExperiment experiment = value.getPayload().getAfter();
			if (experiment.getResults() != null) {
				for (ExperimentResult result : experiment.getResults()) {
					if (Constants.PREDICTOR_RESULT_TYPE.equals(result.getType())) {
						List<String> files = result.getFiles();
						if (files != null) {
							for (int i = 0; i < files.size(); i++) {
								// key
								PredictorResultFileKey key = new PredictorResultFileKey();
								key.setExperimentId(experiment.getId().getOid());
								key.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
								key.setIndex(i);

								// value
								PredictorResultFile predictorResultFile = new PredictorResultFile();
								predictorResultFile.setExperimentId(experiment.getId().getOid());
								predictorResultFile
										.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
								predictorResultFile.setIndex(i);
								predictorResultFile.setFilename(files.get(i));

								list.add(new KeyValue<>(key, predictorResultFile));
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
						List<String> files = result.getFiles();
						if (files != null) {
							for (int i = 0; i < files.size(); i++) {
								// key
								PredictorResultFileKey key = new PredictorResultFileKey();
								key.setExperimentId(mongoId);
								key.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
								key.setIndex(i);

								// value
								PredictorResultFile predictorResultFile = new PredictorResultFile();
								predictorResultFile.setExperimentId(mongoId);
								predictorResultFile
										.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
								predictorResultFile.setIndex(i);
								predictorResultFile.setFilename(files.get(i));

								list.add(new KeyValue<>(key, predictorResultFile));
							}
						}
					}
				}
			}
		}

		return list;
	}

	private StoreBuilder<KeyValueStore<PredictorResultFileKey, PredictorResultFile>> buildPredictorResultFileStore() {
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<PredictorResultFileKey> keyAvroSerde = new SpecificAvroSerde<PredictorResultFileKey>();
		final Serde<PredictorResultFile> valueAvroSerde = new SpecificAvroSerde<PredictorResultFile>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.STORE_CORE_PREDICTOR_RESULT_FILE),
				keyAvroSerde, valueAvroSerde);
	}

	private static class PredictorResultFileTransformSupplier implements
			TransformerSupplier<PredictorResultFileKey, PredictorResultFile, KeyValue<PredictorResultFileKey, PredictorResultFile>> {

		final private String stateStoreName;

		public PredictorResultFileTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<PredictorResultFileKey, PredictorResultFile, KeyValue<PredictorResultFileKey, PredictorResultFile>> get() {
			return new Transformer<PredictorResultFileKey, PredictorResultFile, KeyValue<PredictorResultFileKey, PredictorResultFile>>() {

				private KeyValueStore<PredictorResultFileKey, PredictorResultFile> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<PredictorResultFileKey, PredictorResultFile>) processorContext
							.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<PredictorResultFileKey, PredictorResultFile> transform(PredictorResultFileKey key,
						PredictorResultFile value) {

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
