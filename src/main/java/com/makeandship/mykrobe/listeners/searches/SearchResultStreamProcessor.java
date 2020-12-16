package com.makeandship.mykrobe.listeners.searches;

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
import com.makeandship.mykrobe.models.DebeziumSearch;
import com.makeandship.mykrobe.models.SearchResult;
import com.makeandship.mykrobe.models.SearchResultKey;
import com.makeandship.mykrobe.models.mongo.MongoResult;
import com.makeandship.mykrobe.models.mongo.MongoSearch;
import com.makeandship.mykrobe.models.mongo.MongoSearchResult;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ SearchResultStreamBinding.class })
public class SearchResultStreamProcessor {
	@Autowired
	private ApplicationContext applicationContext;

	public void initializeStateStore() throws Exception {
		StreamsBuilderFactoryBean streamsBuilderFactoryBean = applicationContext
				.getBean("&stream-builder-processSearchResults", StreamsBuilderFactoryBean.class);
		StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();

		StoreBuilder<KeyValueStore<SearchResultKey, SearchResult>> store = buildSearchResultStore();
		streamsBuilder.addStateStore(store);
	}

	@SendTo(SearchResultStreamBinding.SINK_CORE_SEARCH_RESULTS)
	@StreamListener(SearchResultStreamBinding.SOURCE_SEARCH_RESULTS)
	public KStream<SearchResultKey, SearchResult> processSearchResults(KStream<Object, DebeziumSearch> stream) {
		log.info(SearchResultStreamProcessor.class.getName() + "#process: enter");

		try {
			initializeStateStore();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return stream.flatMap(
				new KeyValueMapper<Object, DebeziumSearch, Iterable<KeyValue<SearchResultKey, SearchResult>>>() {
					@Override
					public Iterable<KeyValue<SearchResultKey, SearchResult>> apply(Object key, DebeziumSearch value) {
						return getSearchs(value);
					}
				}).map((key, value) -> new KeyValue<>(key, value))
				.transform(new SearchResultTransformSupplier(Constants.STORE_CORE_GROUP_EXPERIMENT),
						Constants.STORE_CORE_GROUP_EXPERIMENT);
	}

	private Iterable<KeyValue<SearchResultKey, SearchResult>> getSearchs(DebeziumSearch value) {
		List<KeyValue<SearchResultKey, SearchResult>> list = new ArrayList<KeyValue<SearchResultKey, SearchResult>>();

		if (value.getPayload().getAfter() != null) {
			MongoSearch search = value.getPayload().getAfter();
			MongoResult result = search.getResult();
			if (result != null && result.getResults() != null && result.getResults().size() > 0) {
				for (MongoSearchResult mongoSearchResult : result.getResults()) {
					// key
					SearchResultKey key = new SearchResultKey();
					key.setSearchId(search.getId().getOid());
					key.setResultSampleId(mongoSearchResult.getSampleId());

					// value
					SearchResult searchResult = new SearchResult();
					searchResult.setCompletedBigsiQueries(result.getCompletedBigsiQueries());
					searchResult.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
					searchResult.setReference(result.getReference());
					searchResult.setResultGenotype(mongoSearchResult.getGenotype());
					searchResult.setResultSampleId(mongoSearchResult.getSampleId());
					searchResult.setSearchId(search.getId().getOid());
					searchResult.setTotalBigsiQueries(result.getTotalBigsiQueries());
					list.add(new KeyValue<>(key, searchResult));
				}
			}

		} else if (value.getPayload().getPatch() != null) {
			MongoSearch search = value.getPayload().getPatch().getPart();
			MongoResult result = search.getResult();
			if (result != null && result.getResults() != null && result.getResults().size() > 0) {
				for (MongoSearchResult mongoSearchResult : result.getResults()) {
					String mongoId = value.getPayload().getFilter().getId().getOid();
					// key
					SearchResultKey key = new SearchResultKey();
					key.setSearchId(mongoId);
					key.setResultSampleId(mongoSearchResult.getSampleId());

					// value
					SearchResult searchResult = new SearchResult();
					searchResult.setCompletedBigsiQueries(result.getCompletedBigsiQueries());
					searchResult.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
					searchResult.setReference(result.getReference());
					searchResult.setResultGenotype(mongoSearchResult.getGenotype());
					searchResult.setResultSampleId(mongoSearchResult.getSampleId());
					searchResult.setSearchId(mongoId);
					searchResult.setTotalBigsiQueries(result.getTotalBigsiQueries());
					list.add(new KeyValue<>(key, searchResult));
				}
			}
		}

		return list;
	}

	private StoreBuilder<KeyValueStore<SearchResultKey, SearchResult>> buildSearchResultStore() {
		final Map<String, String> serdeConfig = Collections
				.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, Constants.SCHEMA_REGISTRY_URL);

		final Serde<SearchResultKey> keyAvroSerde = new SpecificAvroSerde<SearchResultKey>();
		final Serde<SearchResult> valueAvroSerde = new SpecificAvroSerde<SearchResult>();

		keyAvroSerde.configure(serdeConfig, true);
		valueAvroSerde.configure(serdeConfig, false);

		return Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.STORE_CORE_GROUP_EXPERIMENT),
				keyAvroSerde, valueAvroSerde);
	}

	private static class SearchResultTransformSupplier
			implements TransformerSupplier<SearchResultKey, SearchResult, KeyValue<SearchResultKey, SearchResult>> {

		final private String stateStoreName;

		public SearchResultTransformSupplier(String stateStoreName) {
			this.stateStoreName = stateStoreName;
		}

		@Override
		public Transformer<SearchResultKey, SearchResult, KeyValue<SearchResultKey, SearchResult>> get() {
			return new Transformer<SearchResultKey, SearchResult, KeyValue<SearchResultKey, SearchResult>>() {

				private KeyValueStore<SearchResultKey, SearchResult> stateStore;

				@SuppressWarnings("unchecked")
				@Override
				public void init(ProcessorContext processorContext) {
					stateStore = (KeyValueStore<SearchResultKey, SearchResult>) processorContext
							.getStateStore(stateStoreName);

				}

				@Override
				public KeyValue<SearchResultKey, SearchResult> transform(SearchResultKey key, SearchResult value) {

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
