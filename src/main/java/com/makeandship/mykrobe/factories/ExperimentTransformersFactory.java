package com.makeandship.mykrobe.factories;

import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumExperimentPayload;
import com.makeandship.mykrobe.models.Experiment;
import com.makeandship.mykrobe.models.ExperimentKey;
import com.makeandship.mykrobe.transformers.CreateExperimentTransformer;
import com.makeandship.mykrobe.transformers.DeleteExperimentTransformer;
import com.makeandship.mykrobe.transformers.ExperimentTransformer;
import com.makeandship.mykrobe.transformers.UpdateExperimentTransformer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExperimentTransformersFactory {
	public static final String CREATE = "c";
	public static final String UPDATE = "u";
	public static final String DELETE = "d";
	
	public static ExperimentTransformer create(KeyValueStore<ExperimentKey, Experiment> stateStore,
			DebeziumExperimentPayload payload) {
		switch (payload.getOp()) {
		case "c":
			log.debug("Using CreateExperimentTransformer");
			return new CreateExperimentTransformer(stateStore, payload);
		case "u":
			log.debug("Using UpdateExperimentTransformer");
			return new UpdateExperimentTransformer(stateStore, payload);
		case "d":
			log.debug("Using DeleteExperimentTransformer");
			return new DeleteExperimentTransformer(stateStore, payload);
		default:
			return null;
		}
	}
}
