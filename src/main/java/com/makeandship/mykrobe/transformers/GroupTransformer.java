package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumGroupPayload;
import com.makeandship.mykrobe.models.Group;
import com.makeandship.mykrobe.models.GroupKey;
import com.makeandship.mykrobe.models.mongo.MongoGroup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GroupTransformer {
	protected KeyValueStore<GroupKey, Group> stateStore;
	protected DebeziumGroupPayload payload;

	public GroupTransformer(KeyValueStore<GroupKey, Group> stateStore, DebeziumGroupPayload payload) {
		this.stateStore = stateStore;
		this.payload = payload;
	}

	protected void buildGroup(Group group, String id, MongoGroup mongoGroup) {
		log.debug(GroupTransformer.class.getName() + "#buildGroup: enter");
		group.setId(id);
		group.setName(mongoGroup.getName());
		group.setAnnotation(mongoGroup.getAnnotation());
		group.setSearchId(mongoGroup.getSearch().getOid());

		log.debug(GroupTransformer.class.getName() + "#buildExperiment: exit");
	}

	public abstract KeyValue<GroupKey, Group> transform();
}
