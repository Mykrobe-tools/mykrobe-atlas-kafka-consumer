package com.makeandship.mykrobe.models.serializers;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ISO8601DateSerializer extends StdSerializer<Date> {

	private static final long serialVersionUID = 8704225781948135754L;

	public ISO8601DateSerializer() {
		this(null);
	}

	public ISO8601DateSerializer(Class t) {
		super(t);
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		Instant timestamp = value.toInstant();
		gen.writeString(timestamp.toString());
	}

}
