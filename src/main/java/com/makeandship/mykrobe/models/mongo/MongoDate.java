package com.makeandship.mykrobe.models.mongo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.makeandship.mykrobe.models.deserializers.EpochDateDeserializer;
import com.makeandship.mykrobe.models.serializers.ISO8601DateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoDate {
	@JsonProperty("$date")
	@JsonDeserialize(using = EpochDateDeserializer.class)
	@JsonSerialize(using = ISO8601DateSerializer.class)
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
