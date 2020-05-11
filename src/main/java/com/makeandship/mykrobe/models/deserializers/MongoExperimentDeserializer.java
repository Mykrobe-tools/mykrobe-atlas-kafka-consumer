package com.makeandship.mykrobe.models.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.makeandship.mykrobe.models.mongo.MongoExperiment;

public class MongoExperimentDeserializer extends StdDeserializer<MongoExperiment> {

	private static final long serialVersionUID = -2400755784917423566L;

	public MongoExperimentDeserializer() {
		this(null);
	}

	public MongoExperimentDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public MongoExperiment deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(parser.getText(), MongoExperiment.class);
	}

}
