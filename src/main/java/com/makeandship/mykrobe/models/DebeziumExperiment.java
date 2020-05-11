package com.makeandship.mykrobe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DebeziumExperiment {
	@JsonProperty("payload")
	private DebeziumExperimentPayload payload;
	@JsonProperty("schema")
	private DebeziumSchema schema;

	public DebeziumExperimentPayload getPayload() {
		return payload;
	}

	public void setPayload(DebeziumExperimentPayload payload) {
		this.payload = payload;
	}

	public DebeziumSchema getSchema() {
		return schema;
	}

	public void setSchema(DebeziumSchema schema) {
		this.schema = schema;
	}

}
