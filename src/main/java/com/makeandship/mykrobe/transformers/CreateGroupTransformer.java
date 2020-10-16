package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumGroupPayload;
import com.makeandship.mykrobe.models.Group;
import com.makeandship.mykrobe.models.GroupKey;
import com.makeandship.mykrobe.models.mongo.MongoGroup;

public class CreateGroupTransformer extends GroupTransformer {

	public CreateGroupTransformer(KeyValueStore<GroupKey, Group> stateStore, DebeziumGroupPayload payload) {
		super(stateStore, payload);
	}

	@Override
	public KeyValue<GroupKey, Group> transform() {
		MongoGroup mongoGroup = payload.getAfter();
		GroupKey key = new GroupKey();
		Group group = new Group();
		String id = mongoGroup.getId().getOid();

		// set the key
		key.setId(id);

		// set the value
		buildGroup(group, id, mongoGroup);

		// update the store
		stateStore.put(key, group);
		return KeyValue.pair(key, group);
	}

}
