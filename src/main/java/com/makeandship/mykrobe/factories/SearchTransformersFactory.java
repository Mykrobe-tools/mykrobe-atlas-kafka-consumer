package com.makeandship.mykrobe.factories;

import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumSearchPayload;
import com.makeandship.mykrobe.models.Search;
import com.makeandship.mykrobe.models.SearchKey;
import com.makeandship.mykrobe.transformers.CreateSearchTransformer;
import com.makeandship.mykrobe.transformers.DeleteSearchTransformer;
import com.makeandship.mykrobe.transformers.SearchTransformer;
import com.makeandship.mykrobe.transformers.UpdateSearchTransformer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchTransformersFactory {
	public static final String CREATE = "c";
	public static final String UPDATE = "u";
	public static final String DELETE = "d";

	public static SearchTransformer create(KeyValueStore<SearchKey, Search> stateStore, DebeziumSearchPayload payload) {
		switch (payload.getOp()) {
		case "c":
		case "r":
			log.debug("Using CreateSearchTransformer");
			return new CreateSearchTransformer(stateStore, payload);
		case "u":
			log.debug("Using UpdateSearchTransformer");
			return new UpdateSearchTransformer(stateStore, payload);
		case "d":
			log.debug("Using DeleteSearchTransformer");
			return new DeleteSearchTransformer(stateStore, payload);
		default:
			return null;
		}
	}
}
