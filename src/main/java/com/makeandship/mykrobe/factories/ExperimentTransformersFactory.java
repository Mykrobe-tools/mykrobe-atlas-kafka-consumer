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
	public static final String READ   = "r";
	public static final String UPDATE = "u";
	public static final String DELETE = "d";
	
	public static ExperimentTransformer create(KeyValueStore<ExperimentKey, Experiment> stateStore,
			DebeziumExperimentPayload payload) {
		switch (payload.getOp()) {
		case ExperimentTransformersFactory.CREATE:
			log.debug("Using CreateExperimentTransformer(c)");
			return new CreateExperimentTransformer(stateStore, payload);
		case ExperimentTransformersFactory.READ:
			log.debug("Using CreateExperimentTransformer (r)");
			return new CreateExperimentTransformer(stateStore, payload);
		case ExperimentTransformersFactory.UPDATE:
			log.debug("Using UpdateExperimentTransformer (u)");
			return new UpdateExperimentTransformer(stateStore, payload);
		case ExperimentTransformersFactory.DELETE:
			log.debug("Using DeleteExperimentTransformer (d)");
			return new DeleteExperimentTransformer(stateStore, payload);
		default:
			return null;
		}
	}
}
