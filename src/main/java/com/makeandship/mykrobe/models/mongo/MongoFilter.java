package com.makeandship.mykrobe.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoFilter {
	@JsonProperty("_id")
	private MongoId id;

	public MongoId getId() {
		return id;
	}

	public void setId(MongoId id) {
		this.id = id;
	}

}
