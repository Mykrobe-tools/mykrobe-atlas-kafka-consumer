package com.makeandship.mykrobe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DebeziumSearch {
	@JsonProperty("payload")
	private DebeziumSearchPayload payload;
	@JsonProperty("schema")
	private DebeziumSchema schema;

	public DebeziumSearchPayload getPayload() {
		return payload;
	}

	public void setPayload(DebeziumSearchPayload payload) {
		this.payload = payload;
	}

	public DebeziumSchema getSchema() {
		return schema;
	}

	public void setSchema(DebeziumSchema schema) {
		this.schema = schema;
	}

}
