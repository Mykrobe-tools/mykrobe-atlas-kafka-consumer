package com.makeandship.mykrobe.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoExperimentSet {
	@JsonProperty("$set")
	private MongoExperiment part;

	public MongoExperiment getPart() {
		return part;
	}

	public void setPart(MongoExperiment part) {
		this.part = part;
	}

}
