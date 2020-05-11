package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumExperimentPayload;
import com.makeandship.mykrobe.models.Experiment;
import com.makeandship.mykrobe.models.ExperimentKey;
import com.makeandship.mykrobe.models.mongo.MongoExperiment;

public class CreateExperimentTransformer extends ExperimentTransformer {

	public CreateExperimentTransformer(KeyValueStore<ExperimentKey, Experiment> stateStore,
			DebeziumExperimentPayload payload) {
		super(stateStore, payload);
	}

	@Override
	public KeyValue<ExperimentKey, Experiment> transform() {
		MongoExperiment mongoExperiment = payload.getAfter();
		ExperimentKey key = new ExperimentKey();
		Experiment experiment = new Experiment();
		String id = mongoExperiment.getId().getOid();

		// set the key
		key.setId(id);

		// set the value
		buildExperiment(experiment, id, mongoExperiment);

		// update the store
		stateStore.put(key, experiment);
		return KeyValue.pair(key, experiment);
	}

}
