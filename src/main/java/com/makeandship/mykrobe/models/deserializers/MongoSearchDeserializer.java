package com.makeandship.mykrobe.models.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.makeandship.mykrobe.models.mongo.MongoSearch;

public class MongoSearchDeserializer extends StdDeserializer<MongoSearch> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3304327445414213264L;

	public MongoSearchDeserializer() {
		this(null);
	}

	public MongoSearchDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public MongoSearch deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(parser.getText(), MongoSearch.class);
	}

}
