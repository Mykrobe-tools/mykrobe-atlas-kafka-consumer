package com.makeandship.mykrobe.models.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makeandship.mykrobe.models.mongo.MongoDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataSample {
	@JsonProperty("labId")
	private String labId;
	@JsonProperty("isolateId")
	private String isolateId;
	@JsonProperty("collectionDate")
	private MongoDate collectionDate;
	@JsonProperty("prospectiveIsolate")
	private String prospectiveIsolate;
	@JsonProperty("countryIsolate")
	private String countryIsolate;
	@JsonProperty("cityIsolate")
	private String cityIsolate;
	@JsonProperty("longitudeIsolate")
	private Double longitudeIsolate;
	@JsonProperty("latitudeIsolate")
	private Double latitudeIsolate;
	@JsonProperty("dateArrived")
	private MongoDate dateArrived;
	@JsonProperty("anatomicalOrigin")
	private String anatomicalOrigin;
	@JsonProperty("smear")
	private String smear;

	public String getLabId() {
		return labId;
	}

	public void setLabId(String labId) {
		this.labId = labId;
	}

	public String getIsolateId() {
		return isolateId;
	}

	public void setIsolateId(String isolateId) {
		this.isolateId = isolateId;
	}

	public MongoDate getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(MongoDate collectionDate) {
		this.collectionDate = collectionDate;
	}

	public String getProspectiveIsolate() {
		return prospectiveIsolate;
	}

	public void setProspectiveIsolate(String prospectiveIsolate) {
		this.prospectiveIsolate = prospectiveIsolate;
	}

	public String getCountryIsolate() {
		return countryIsolate;
	}

	public void setCountryIsolate(String countryIsolate) {
		this.countryIsolate = countryIsolate;
	}

	public String getCityIsolate() {
		return cityIsolate;
	}

	public void setCityIsolate(String cityIsolate) {
		this.cityIsolate = cityIsolate;
	}

	public Double getLongitudeIsolate() {
		return longitudeIsolate;
	}

	public void setLongitudeIsolate(Double longitudeIsolate) {
		this.longitudeIsolate = longitudeIsolate;
	}

	public Double getLatitudeIsolate() {
		return latitudeIsolate;
	}

	public void setLatitudeIsolate(Double latitudeIsolate) {
		this.latitudeIsolate = latitudeIsolate;
	}

	public MongoDate getDateArrived() {
		return dateArrived;
	}

	public void setDateArrived(MongoDate dateArrived) {
		this.dateArrived = dateArrived;
	}

	public String getAnatomicalOrigin() {
		return anatomicalOrigin;
	}

	public void setAnatomicalOrigin(String anatomicalOrigin) {
		this.anatomicalOrigin = anatomicalOrigin;
	}

	public String getSmear() {
		return smear;
	}

	public void setSmear(String smear) {
		this.smear = smear;
	}
}
