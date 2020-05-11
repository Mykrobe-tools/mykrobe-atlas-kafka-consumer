package com.makeandship.mykrobe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.makeandship.mykrobe.models.deserializers.MongoExperimentDeserializer;
import com.makeandship.mykrobe.models.deserializers.MongoExperimentSetDeserializer;
import com.makeandship.mykrobe.models.deserializers.MongoFilterDeserializer;
import com.makeandship.mykrobe.models.mongo.MongoExperiment;
import com.makeandship.mykrobe.models.mongo.MongoExperimentSet;
import com.makeandship.mykrobe.models.mongo.MongoFilter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DebeziumExperimentPayload {
	@JsonProperty("after")
	@JsonDeserialize(using = MongoExperimentDeserializer.class)
	private MongoExperiment after;
	@JsonProperty("before")
	@JsonDeserialize(using = MongoExperimentDeserializer.class)
	private MongoExperiment before;
	@JsonProperty("patch")
	@JsonDeserialize(using = MongoExperimentSetDeserializer.class)
	private MongoExperimentSet patch;
	@JsonProperty("filter")
	@JsonDeserialize(using = MongoFilterDeserializer.class)
	private MongoFilter filter;
	@JsonProperty("op")
	private String op;

	public MongoExperiment getAfter() {
		return after;
	}

	public void setAfter(MongoExperiment after) {
		this.after = after;
	}

	public MongoExperiment getBefore() {
		return before;
	}

	public void setBefore(MongoExperiment before) {
		this.before = before;
	}

	public MongoExperimentSet getPatch() {
		return patch;
	}

	public void setPatch(MongoExperimentSet patch) {
		this.patch = patch;
	}

	public MongoFilter getFilter() {
		return filter;
	}

	public void setFilter(MongoFilter filter) {
		this.filter = filter;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}
