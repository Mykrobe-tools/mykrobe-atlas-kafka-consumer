package com.makeandship.mykrobe.models.deserializers;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EpochDateDeserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = 6745505295242059963L;

	public EpochDateDeserializer() {
		this(null);
	}

	public EpochDateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		long timestamp = jsonparser.getLongValue();
		log.debug("Deserialize data with epoch in mills: " + timestamp);
		if (timestamp == 0) {
			return null;
		} else {
			return Date.from(Instant.ofEpochMilli(timestamp));
		}
	}
}
