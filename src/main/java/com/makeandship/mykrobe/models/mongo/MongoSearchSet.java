package com.makeandship.mykrobe.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoSearchSet {
	@JsonProperty("$set")
	private MongoSearch part;

	public MongoSearch getPart() {
		return part;
	}

	public void setPart(MongoSearch part) {
		this.part = part;
	}

}
