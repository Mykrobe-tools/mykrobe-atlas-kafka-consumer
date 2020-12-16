package com.makeandship.mykrobe.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoBigsi {
	@JsonProperty("type")
	private String type;
	@JsonProperty("query")
	private MongoQuery query;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MongoQuery getQuery() {
		return query;
	}

	public void setQuery(MongoQuery query) {
		this.query = query;
	}

}
