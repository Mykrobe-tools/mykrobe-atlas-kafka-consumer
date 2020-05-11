package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.converters.DateConverter;
import com.makeandship.mykrobe.models.DebeziumExperimentPayload;
import com.makeandship.mykrobe.models.Experiment;
import com.makeandship.mykrobe.models.ExperimentKey;
import com.makeandship.mykrobe.models.ExperimentMetadata;
import com.makeandship.mykrobe.models.metadata.MetadataOutcome;
import com.makeandship.mykrobe.models.metadata.MetadataPatient;
import com.makeandship.mykrobe.models.metadata.MetadataSample;
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
		experiment.setId(id);
		experiment.setFile(mongoExperiment.getFile());
		experiment.setCreated(DateConverter.dateToMysql(mongoExperiment.getCreated().getDate()));
		experiment.setModified(DateConverter.dateToMysql(mongoExperiment.getModified().getDate()));

		// metadata
		if (mongoExperiment.getMetadata() != null) {
			ExperimentMetadata metadata = mongoExperiment.getMetadata();
			if (metadata.getOutcome() != null) {
				MetadataOutcome outcome = metadata.getOutcome();
				if (outcome.getDateOfDeath() != null) {
					experiment.setOutcomeDateOfDeath(DateConverter.dateToMysql(outcome.getDateOfDeath().getDate()));
				}
				experiment.setOutcomeSputumCultureConversion(outcome.getSputumCultureConversion());
				experiment.setOutcomeSputumSmearConversion(outcome.getSputumSmearConversion());
				experiment.setOutcomeWhoOutcomeCategory(outcome.getWhoOutcomeCategory());
			}

			if (metadata.getPatient() != null) {
				MetadataPatient patient = metadata.getPatient();
				experiment.setPatientAge(patient.getAge());
				experiment.setPatientArt(patient.getArt());
				experiment.setPatientBmi(patient.getBmi());
				experiment.setPatientCountryOfBirth(patient.getCountryOfBirth());
				experiment.setPatientDiabetic(patient.getDiabetic());
				experiment.setPatientGenderAtBirth(patient.getGenderAtBirth());
				experiment.setPatientHivStatus(patient.getHivStatus());
				experiment.setPatientHomeless(patient.getHomeless());
				experiment.setPatientId(patient.getPatientId());
				experiment.setPatientImprisoned(patient.getImprisoned());
				experiment.setPatientInjectingDrugUse(patient.getInjectingDrugUse());
				experiment.setPatientSiteId(patient.getSiteId());
				experiment.setPatientSmoker(patient.getSmoker());
			}

			if (metadata.getSample() != null) {
				MetadataSample sample = metadata.getSample();
				experiment.setSampleAnatomicalOrigin(sample.getAnatomicalOrigin());
				experiment.setSampleCityIsolate(sample.getCityIsolate());
				if (sample.getCollectionDate() != null) {
					experiment.setSampleCollectionDate(DateConverter.dateToMysql(sample.getCollectionDate().getDate()));
				}
				experiment.setSampleCountryIsolate(sample.getCountryIsolate());
				if (sample.getDateArrived() != null) {
					experiment.setSampleDateArrived(DateConverter.dateToMysql(sample.getDateArrived().getDate()));
				}
				experiment.setSampleIsolateId(sample.getIsolateId());
				experiment.setSampleLabId(sample.getLabId());
				experiment.setSampleLatitudeIsolate(sample.getLatitudeIsolate());
				experiment.setSampleLongitudeIsolate(sample.getLongitudeIsolate());
				experiment.setSampleProspectiveIsolate(sample.getProspectiveIsolate());
				experiment.setSampleSmear(sample.getSmear());
			}
		}

		// update the store
		stateStore.put(key, experiment);
		return KeyValue.pair(key, experiment);
	}

}
