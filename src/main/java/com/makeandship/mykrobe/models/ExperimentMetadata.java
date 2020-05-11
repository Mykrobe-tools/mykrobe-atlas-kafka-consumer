package com.makeandship.mykrobe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makeandship.mykrobe.models.metadata.MetadataOutcome;
import com.makeandship.mykrobe.models.metadata.MetadataPatient;
import com.makeandship.mykrobe.models.metadata.MetadataSample;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperimentMetadata {
	@JsonProperty("outcome")
	private MetadataOutcome outcome;
	@JsonProperty("sample")
	private MetadataSample sample;
	@JsonProperty("patient")
	private MetadataPatient patient;

	public MetadataOutcome getOutcome() {
		return outcome;
	}

	public void setOutcome(MetadataOutcome outcome) {
		this.outcome = outcome;
	}

	public MetadataSample getSample() {
		return sample;
	}

	public void setSample(MetadataSample sample) {
		this.sample = sample;
	}

	public MetadataPatient getPatient() {
		return patient;
	}

	public void setPatient(MetadataPatient patient) {
		this.patient = patient;
	}

}
