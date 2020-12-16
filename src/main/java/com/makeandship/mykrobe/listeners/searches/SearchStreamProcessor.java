package com.makeandship.mykrobe.listeners.searches;

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
import com.makeandship.mykrobe.factories.SearchTransformersFactory;
import com.makeandship.mykrobe.models.DebeziumSearch;
import com.makeandship.mykrobe.models.DebeziumSearchPayload;
import com.makeandship.mykrobe.models.Search;
import com.makeandship.mykrobe.models.SearchKey;
import com.makeandship.mykrobe.transformers.SearchTransformer;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ SearchStreamBinding.class })
public class SearchStreamProcessor {

	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processCoreSearchs", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<SearchKey, Search>> store = buildSearchStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo(SearchStreamBinding.SINK_CORE_SEARCHES)
	@StreamListener(SearchStreamBinding.SOURCE_SEARCHES)
	public KStream<SearchKey, Search> processCoreSearchs(KStream<Object, DebeziumSearch> stream) {
		log.debug(SearchStreamProcessor.class.getName() + "#processCoreSearchs: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return stream.transform(new SearchTransformSupplier(Constants.STORE_CORE_GROUP), Constants.STORE_CORE_GROUP);
	}

	private StoreBuilder<KeyValueStore<SearchKey, Search>> buildSearchStore() {
		log.debug(SearchStreamProcessor.class.getName() + "#buildSearchStore: enter");
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<SearchKey> keyAvroSerde = new SpecificAvroSerde<SearchKey>();
		final Serde<Search> valueAvroSerde = new SpecificAvroSerde<Search>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		log.debug(SearchStreamProcessor.class.getName() + "#buildSearchStore: exit");

		return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.STORE_CORE_GROUP), keyAvroSerde,
				valueAvroSerde);
	}

	private static class SearchTransformSupplier
			implements TransformerSupplier<Object, DebeziumSearch, KeyValue<SearchKey, Search>> {

		final private String stateStoreName;

		public SearchTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<Object, DebeziumSearch, KeyValue<SearchKey, Search>> get() {
			return new Transformer<Object, DebeziumSearch, KeyValue<SearchKey, Search>>() {

				private KeyValueStore<SearchKey, Search> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<SearchKey, Search>) processorContext.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<SearchKey, Search> transform(Object key, DebeziumSearch value) {
					log.debug(SearchTransformSupplier.class.getName() + "#transform: enter");

					DebeziumSearchPayload payload = value.getPayload();
					SearchTransformer transformer = SearchTransformersFactory.create(stateStore, payload);

					if (transformer != null) {
						log.debug(SearchTransformSupplier.class.getName() + "#transform: call transform");
						return transformer.transform();
					}

					log.debug(SearchTransformSupplier.class.getName() + "#transform: exit (null)");

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
