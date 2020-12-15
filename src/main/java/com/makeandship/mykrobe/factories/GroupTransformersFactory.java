package com.makeandship.mykrobe.factories;

import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumGroupPayload;
import com.makeandship.mykrobe.models.Group;
import com.makeandship.mykrobe.models.GroupKey;
import com.makeandship.mykrobe.transformers.CreateGroupTransformer;
import com.makeandship.mykrobe.transformers.DeleteGroupTransformer;
import com.makeandship.mykrobe.transformers.GroupTransformer;
import com.makeandship.mykrobe.transformers.UpdateGroupTransformer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupTransformersFactory {
	public static final String CREATE = "c";
	public static final String UPDATE = "u";
	public static final String DELETE = "d";

	public static GroupTransformer create(KeyValueStore<GroupKey, Group> stateStore, DebeziumGroupPayload payload) {
		switch (payload.getOp()) {
		case "c":
		case "r":
			log.debug("Using CreateGroupTransformer");
			return new CreateGroupTransformer(stateStore, payload);
		case "u":
			log.debug("Using UpdateGroupTransformer");
			return new UpdateGroupTransformer(stateStore, payload);
		case "d":
			log.debug("Using DeleteGroupTransformer");
			return new DeleteGroupTransformer(stateStore, payload);
		default:
			return null;
		}
	}
}
