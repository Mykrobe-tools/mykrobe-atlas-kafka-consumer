package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumExperimentPayload;
import com.makeandship.mykrobe.models.Experiment;
import com.makeandship.mykrobe.models.ExperimentKey;

public abstract class ExperimentTransformer {
	protected KeyValueStore<ExperimentKey, Experiment> stateStore;
	protected DebeziumExperimentPayload payload;

	public ExperimentTransformer(KeyValueStore<ExperimentKey, Experiment> stateStore,
			DebeziumExperimentPayload payload) {
		this.stateStore = stateStore;
		this.payload = payload;
	}

	public abstract KeyValue<ExperimentKey, Experiment> transform();
}
