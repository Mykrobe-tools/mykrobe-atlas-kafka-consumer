package com.makeandship.mykrobe.models.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.makeandship.mykrobe.models.mongo.MongoGroup;

public class MongoGroupDeserializer extends StdDeserializer<MongoGroup> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3304327445414213264L;

	public MongoGroupDeserializer() {
		this(null);
	}

	public MongoGroupDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public MongoGroup deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(parser.getText(), MongoGroup.class);
	}

}
