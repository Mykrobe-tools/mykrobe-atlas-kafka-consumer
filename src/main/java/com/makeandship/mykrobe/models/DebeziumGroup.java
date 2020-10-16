package com.makeandship.mykrobe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DebeziumGroup {
	@JsonProperty("payload")
	private DebeziumGroupPayload payload;
	@JsonProperty("schema")
	private DebeziumSchema schema;

	public DebeziumGroupPayload getPayload() {
		return payload;
	}

	public void setPayload(DebeziumGroupPayload payload) {
		this.payload = payload;
	}

	public DebeziumSchema getSchema() {
		return schema;
	}

	public void setSchema(DebeziumSchema schema) {
		this.schema = schema;
	}

}
