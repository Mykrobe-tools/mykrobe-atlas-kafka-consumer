package com.makeandship.mykrobe.models.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.makeandship.mykrobe.models.mongo.MongoSearchSet;

public class MongoSearchSetDeserializer extends StdDeserializer<MongoSearchSet> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2365903526240567170L;

	public MongoSearchSetDeserializer() {
		this(null);
	}

	public MongoSearchSetDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public MongoSearchSet deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(parser.getText(), MongoSearchSet.class);
	}

}
