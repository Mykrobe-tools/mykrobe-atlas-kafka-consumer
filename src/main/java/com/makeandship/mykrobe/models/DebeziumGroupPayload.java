package com.makeandship.mykrobe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.makeandship.mykrobe.models.deserializers.MongoFilterDeserializer;
import com.makeandship.mykrobe.models.deserializers.MongoGroupDeserializer;
import com.makeandship.mykrobe.models.deserializers.MongoGroupSetDeserializer;
import com.makeandship.mykrobe.models.mongo.MongoFilter;
import com.makeandship.mykrobe.models.mongo.MongoGroup;
import com.makeandship.mykrobe.models.mongo.MongoGroupSet;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DebeziumGroupPayload {
	@JsonProperty("after")
	@JsonDeserialize(using = MongoGroupDeserializer.class)
	private MongoGroup after;
	@JsonProperty("before")
	@JsonDeserialize(using = MongoGroupDeserializer.class)
	private MongoGroup before;
	@JsonProperty("patch")
	@JsonDeserialize(using = MongoGroupSetDeserializer.class)
	private MongoGroupSet patch;
	@JsonProperty("filter")
	@JsonDeserialize(using = MongoFilterDeserializer.class)
	private MongoFilter filter;
	@JsonProperty("op")
	private String op;

	public MongoGroup getAfter() {
		return after;
	}

	public void setAfter(MongoGroup after) {
		this.after = after;
	}

	public MongoGroup getBefore() {
		return before;
	}

	public void setBefore(MongoGroup before) {
		this.before = before;
	}

	public MongoGroupSet getPatch() {
		return patch;
	}

	public void setPatch(MongoGroupSet patch) {
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
