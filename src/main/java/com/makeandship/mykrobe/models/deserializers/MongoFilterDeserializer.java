package com.makeandship.mykrobe.models.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.makeandship.mykrobe.models.mongo.MongoFilter;

public class MongoFilterDeserializer extends StdDeserializer<MongoFilter> {

	private static final long serialVersionUID = 8578160884072394693L;

	public MongoFilterDeserializer() {
		this(null);
	}

	public MongoFilterDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public MongoFilter deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(parser.getText(), MongoFilter.class);
	}

}
