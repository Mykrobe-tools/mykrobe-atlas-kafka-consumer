package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.models.DebeziumExperimentPayload;
import com.makeandship.mykrobe.models.Experiment;
import com.makeandship.mykrobe.models.ExperimentKey;
import com.makeandship.mykrobe.models.mongo.MongoExperiment;
import com.makeandship.mykrobe.models.mongo.MongoFilter;

public class DeleteExperimentTransformer extends ExperimentTransformer {

	public DeleteExperimentTransformer(KeyValueStore<ExperimentKey, Experiment> stateStore,
			DebeziumExperimentPayload payload) {
		super(stateStore, payload);
	}

	@Override
	public KeyValue<ExperimentKey, Experiment> transform() {
		MongoExperiment mongoExperiment = payload.getPatch().getPart();
		MongoFilter mongoFilter = payload.getFilter();

		ExperimentKey key = new ExperimentKey();
		String id = mongoFilter.getId().getOid();

		// set the key
		key.setId(id);

		Experiment experiment = stateStore.get(key);

		if (experiment == null) {
			experiment = new Experiment();
		}

		// set the value
		experiment.setId(id);
		experiment.setDeleted(1);

		// update the store
		stateStore.put(key, experiment);

		return KeyValue.pair(key, experiment);
	}

}
