package com.makeandship.mykrobe.models.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makeandship.mykrobe.models.mongo.MongoDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataOutcome {
	@JsonProperty("sputumSmearConversion")
	private String sputumSmearConversion;
	@JsonProperty("sputumCultureConversion")
	private String sputumCultureConversion;
	@JsonProperty("whoOutcomeCategory")
	private String whoOutcomeCategory;
	@JsonProperty("dateOfDeath")
	private MongoDate dateOfDeath;

	public String getSputumSmearConversion() {
		return sputumSmearConversion;
	}

	public void setSputumSmearConversion(String sputumSmearConversion) {
		this.sputumSmearConversion = sputumSmearConversion;
	}

	public String getSputumCultureConversion() {
		return sputumCultureConversion;
	}

	public void setSputumCultureConversion(String sputumCultureConversion) {
		this.sputumCultureConversion = sputumCultureConversion;
	}

	public String getWhoOutcomeCategory() {
		return whoOutcomeCategory;
	}

	public void setWhoOutcomeCategory(String whoOutcomeCategory) {
		this.whoOutcomeCategory = whoOutcomeCategory;
	}

	public MongoDate getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(MongoDate dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

}
