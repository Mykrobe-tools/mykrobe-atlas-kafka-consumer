package com.makeandship.mykrobe.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoGroupSet {
	@JsonProperty("$set")
	private MongoGroup part;

	public MongoGroup getPart() {
		return part;
	}

	public void setPart(MongoGroup part) {
		this.part = part;
	}

}
