package com.makeandship.mykrobe.factories;

import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumExperimentPayload;
import com.makeandship.mykrobe.models.Experiment;
import com.makeandship.mykrobe.models.ExperimentKey;
import com.makeandship.mykrobe.transformers.CreateExperimentTransformer;
import com.makeandship.mykrobe.transformers.DeleteExperimentTransformer;
import com.makeandship.mykrobe.transformers.ExperimentTransformer;
import com.makeandship.mykrobe.transformers.UpdateExperimentTransformer;

public class ExperimentTransformersFactory {
	public static ExperimentTransformer create(KeyValueStore<ExperimentKey, Experiment> stateStore,
			DebeziumExperimentPayload payload) {
		switch (payload.getOp()) {
		case "r":
			return new CreateExperimentTransformer(stateStore, payload);
		case "u":
			return new UpdateExperimentTransformer(stateStore, payload);
		case "d":
			return new DeleteExperimentTransformer(stateStore, payload);
		default:
			return null;
		}
	}
}
