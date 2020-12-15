package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumGroupPayload;
import com.makeandship.mykrobe.models.Group;
import com.makeandship.mykrobe.models.GroupKey;
import com.makeandship.mykrobe.models.mongo.MongoFilter;

public class DeleteGroupTransformer extends GroupTransformer {

	public DeleteGroupTransformer(KeyValueStore<GroupKey, Group> stateStore, DebeziumGroupPayload payload) {
		super(stateStore, payload);
	}

	@Override
	public KeyValue<GroupKey, Group> transform() {
		MongoFilter mongoFilter = payload.getFilter();

		GroupKey key = new GroupKey();
		String id = mongoFilter.getId().getOid();

		// set the key
		key.setId(id);

		Group group = stateStore.get(key);

		if (group == null) {
			group = new Group();
		}

		// set the value
		group.setId(id);
		group.setDeleted(1);

		// update the store
		stateStore.put(key, group);

		return KeyValue.pair(key, group);
	}

}
