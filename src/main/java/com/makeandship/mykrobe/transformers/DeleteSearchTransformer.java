package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumSearchPayload;
import com.makeandship.mykrobe.models.Search;
import com.makeandship.mykrobe.models.SearchKey;
import com.makeandship.mykrobe.models.mongo.MongoFilter;

public class DeleteSearchTransformer extends SearchTransformer {

	public DeleteSearchTransformer(KeyValueStore<SearchKey, Search> stateStore, DebeziumSearchPayload payload) {
		super(stateStore, payload);
	}

	@Override
	public KeyValue<SearchKey, Search> transform() {
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
		search.setId(id);
		search.setDeleted(1);

		// update the store
		stateStore.put(key, search);

		return KeyValue.pair(key, search);
	}

}
