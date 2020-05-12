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
import org.apache.kafka.streams.kstream.Predicate;
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
import com.makeandship.mykrobe.models.DistanceResult;
import com.makeandship.mykrobe.models.DistanceResultKey;
import com.makeandship.mykrobe.models.ExperimentResult;
import com.makeandship.mykrobe.models.mongo.MongoExperiment;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ DistanceResultStreamBinding.class })
public class DistanceResultStreamProcessor {
	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processDistanceResult", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<DistanceResultKey, DistanceResult>> store = buildDistanceResultStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo({ DistanceResultStreamBinding.SINK_CORE_NEAREST_NEIGHBOUR_RESULT,
			DistanceResultStreamBinding.SINK_CORE_TREE_DISTANCE_RESULT })
	@StreamListener(DistanceResultStreamBinding.SOURCE_DISTANCE_RESULT)
	public KStream<DistanceResultKey, DistanceResult>[] processDistanceResult(
			KStream<Object, DebeziumExperiment> stream) {
		log.info(DistanceResultStreamProcessor.class.getName() + "#process: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Predicate<DistanceResultKey, DistanceResult> isNearestNeighbourResult = (k,
				v) -> (v != null && Constants.NEAREST_NEIGHBOUR_SUBTYPE.equals(v.getSubType()));
		Predicate<DistanceResultKey, DistanceResult> isTreeDistanceResult = (k,
				v) -> (v != null && Constants.TREE_DISTANCE_SUBTYPE.equals(v.getSubType()));

		return stream.flatMap(
				new KeyValueMapper<Object, DebeziumExperiment, Iterable<KeyValue<DistanceResultKey, DistanceResult>>>() {
					@Override
					public Iterable<KeyValue<DistanceResultKey, DistanceResult>> apply(Object key,
							DebeziumExperiment value) {
						return getResults(value);
					}
				}).map((key, value) -> new KeyValue<>(key, value))
				.transform(new DistanceResultTransformSupplier(Constants.STORE_CORE_DISTANCE_RESULT),
						Constants.STORE_CORE_DISTANCE_RESULT)
				.branch(isNearestNeighbourResult, isTreeDistanceResult);
	}

	private Iterable<KeyValue<DistanceResultKey, DistanceResult>> getResults(DebeziumExperiment value) {
		List<KeyValue<DistanceResultKey, DistanceResult>> list = new ArrayList<KeyValue<DistanceResultKey, DistanceResult>>();

		if (value.getPayload().getAfter() != null) {
			MongoExperiment experiment = value.getPayload().getAfter();
			if (experiment.getResults() != null) {
				for (ExperimentResult result : experiment.getResults()) {
					if (Constants.DISTANCE_RESULT_TYPE.equals(result.getType())) {
						// key
						DistanceResultKey key = new DistanceResultKey();
						key.setExperimentId(experiment.getId().getOid());
						key.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
						key.setSubType(result.getSubType());

						// value
						DistanceResult distanceResult = ResultConverter.toDistanceResult(experiment.getId().getOid(),
								result);
						list.add(new KeyValue<>(key, distanceResult));
					}
				}
			}

		} else if (value.getPayload().getPatch() != null) {
			MongoExperiment experiment = value.getPayload().getPatch().getPart();
			if (experiment.getResults() != null) {
				for (ExperimentResult result : experiment.getResults()) {
					if (Constants.DISTANCE_RESULT_TYPE.equals(result.getType())) {
						String mongoId = value.getPayload().getFilter().getId().getOid();
						// key
						DistanceResultKey key = new DistanceResultKey();
						key.setExperimentId(mongoId);
						key.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
						key.setSubType(result.getSubType());

						// value
						DistanceResult distanceResult = ResultConverter.toDistanceResult(mongoId, result);
						list.add(new KeyValue<>(key, distanceResult));
					}
				}
			}
		}

		return list;
	}

	private StoreBuilder<KeyValueStore<DistanceResultKey, DistanceResult>> buildDistanceResultStore() {
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<DistanceResultKey> keyAvroSerde = new SpecificAvroSerde<DistanceResultKey>();
		final Serde<DistanceResult> valueAvroSerde = new SpecificAvroSerde<DistanceResult>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.STORE_CORE_DISTANCE_RESULT),
				keyAvroSerde, valueAvroSerde);
	}

	private static class DistanceResultTransformSupplier implements
			TransformerSupplier<DistanceResultKey, DistanceResult, KeyValue<DistanceResultKey, DistanceResult>> {

		final private String stateStoreName;

		public DistanceResultTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<DistanceResultKey, DistanceResult, KeyValue<DistanceResultKey, DistanceResult>> get() {
			return new Transformer<DistanceResultKey, DistanceResult, KeyValue<DistanceResultKey, DistanceResult>>() {

				private KeyValueStore<DistanceResultKey, DistanceResult> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<DistanceResultKey, DistanceResult>) processorContext
							.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<DistanceResultKey, DistanceResult> transform(DistanceResultKey key,
						DistanceResult value) {

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
