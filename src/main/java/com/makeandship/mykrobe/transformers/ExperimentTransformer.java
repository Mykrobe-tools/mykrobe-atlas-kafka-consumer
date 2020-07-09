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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ExperimentTransformer {
	protected KeyValueStore<ExperimentKey, Experiment> stateStore;
	protected DebeziumExperimentPayload payload;

	public ExperimentTransformer(KeyValueStore<ExperimentKey, Experiment> stateStore,
			DebeziumExperimentPayload payload) {
		this.stateStore = stateStore;
		this.payload = payload;
	}

	protected void buildExperiment(Experiment experiment, String id, MongoExperiment mongoExperiment) {
		log.debug(ExperimentTransformer.class.getName() + "#buildExperiment: enter");
		experiment.setId(id);
		experiment.setFile(mongoExperiment.getFile());
		if (mongoExperiment.getCreated() != null) {
			experiment.setCreated(DateConverter.dateToMysql(mongoExperiment.getCreated().getDate()));
		}

		if (mongoExperiment.getModified() != null) {
			experiment.setModified(DateConverter.dateToMysql(mongoExperiment.getModified().getDate()));
		}

		// metadata
		if (mongoExperiment.getMetadata() != null) {
			ExperimentMetadata metadata = mongoExperiment.getMetadata();
			if (metadata.getOutcome() != null) {
				log.debug(ExperimentTransformer.class.getName() + "#buildExperiment: Outcome metadata");
				
				MetadataOutcome outcome = metadata.getOutcome();
				if (outcome.getDateOfDeath() != null) {
					experiment.setOutcomeDateOfDeath(DateConverter.dateToMysql(outcome.getDateOfDeath().getDate()));
				}
				experiment.setOutcomeSputumCultureConversion(outcome.getSputumCultureConversion());
				experiment.setOutcomeSputumSmearConversion(outcome.getSputumSmearConversion());
				experiment.setOutcomeWhoOutcomeCategory(outcome.getWhoOutcomeCategory());
			}

			if (metadata.getPatient() != null) {
				log.debug(ExperimentTransformer.class.getName() + "#buildExperiment: Patient metadata");
				
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
				log.debug(ExperimentTransformer.class.getName() + "#buildExperiment: Sample metadata");
				
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
		
		log.debug(ExperimentTransformer.class.getName() + "#buildExperiment: exit");
	}

	public abstract KeyValue<ExperimentKey, Experiment> transform();
}
