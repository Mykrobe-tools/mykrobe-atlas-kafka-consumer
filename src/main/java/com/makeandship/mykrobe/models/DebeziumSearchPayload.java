package com.makeandship.mykrobe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.makeandship.mykrobe.models.deserializers.MongoFilterDeserializer;
import com.makeandship.mykrobe.models.deserializers.MongoSearchDeserializer;
import com.makeandship.mykrobe.models.deserializers.MongoSearchSetDeserializer;
import com.makeandship.mykrobe.models.mongo.MongoFilter;
import com.makeandship.mykrobe.models.mongo.MongoSearch;
import com.makeandship.mykrobe.models.mongo.MongoSearchSet;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DebeziumSearchPayload {
	@JsonProperty("after")
	@JsonDeserialize(using = MongoSearchDeserializer.class)
	private MongoSearch after;
	@JsonProperty("before")
	@JsonDeserialize(using = MongoSearchDeserializer.class)
	private MongoSearch before;
	@JsonProperty("patch")
	@JsonDeserialize(using = MongoSearchSetDeserializer.class)
	private MongoSearchSet patch;
	@JsonProperty("filter")
	@JsonDeserialize(using = MongoFilterDeserializer.class)
	private MongoFilter filter;
	@JsonProperty("op")
	private String op;

	public MongoSearch getAfter() {
		return after;
	}

	public void setAfter(MongoSearch after) {
		this.after = after;
	}

	public MongoSearch getBefore() {
		return before;
	}

	public void setBefore(MongoSearch before) {
		this.before = before;
	}

	public MongoSearchSet getPatch() {
		return patch;
	}

	public void setPatch(MongoSearchSet patch) {
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
