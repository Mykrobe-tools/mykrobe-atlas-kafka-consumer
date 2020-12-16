package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumSearchPayload;
import com.makeandship.mykrobe.models.Search;
import com.makeandship.mykrobe.models.SearchKey;
import com.makeandship.mykrobe.models.mongo.MongoFilter;
import com.makeandship.mykrobe.models.mongo.MongoSearch;

public class UpdateSearchTransformer extends SearchTransformer {

	public UpdateSearchTransformer(KeyValueStore<SearchKey, Search> stateStore, DebeziumSearchPayload payload) {
		super(stateStore, payload);
	}

	@Override
	public KeyValue<SearchKey, Search> transform() {
		MongoSearch mongoSearch = payload.getPatch().getPart();
		MongoFilter mongoFilter = payload.getFilter();

		SearchKey key = new SearchKey();
		String id = mongoFilter.getId().getOid();

		// set the key
		key.setId(id);

		Search search = stateStore.get(key);

		if (search == null) {
			search = new Search();
		}

		// set the value
		buildSearch(search, id, mongoSearch);

		// update the store
		stateStore.put(key, search);

		return KeyValue.pair(key, search);
	}

}
