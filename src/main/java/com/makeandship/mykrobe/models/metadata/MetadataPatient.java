package com.makeandship.mykrobe.models.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataPatient {
	@JsonProperty("patientId")
	private String patientId;
	@JsonProperty("siteId")
	private String siteId;
	@JsonProperty("genderAtBirth")
	private String genderAtBirth;
	@JsonProperty("countryOfBirth")
	private String countryOfBirth;
	@JsonProperty("age")
	private Integer age;
	@JsonProperty("bmi")
	private Double bmi;
	@JsonProperty("injectingDrugUse")
	private String injectingDrugUse;
	@JsonProperty("homeless")
	private String homeless;
	@JsonProperty("imprisoned")
	private String imprisoned;
	@JsonProperty("smoker")
	private String smoker;
	@JsonProperty("diabetic")
	private String diabetic;
	@JsonProperty("hivStatus")
	private String hivStatus;
	@JsonProperty("art")
	private String art;

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getGenderAtBirth() {
		return genderAtBirth;
	}

	public void setGenderAtBirth(String genderAtBirth) {
		this.genderAtBirth = genderAtBirth;
	}

	public String getCountryOfBirth() {
		return countryOfBirth;
	}

	public void setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getBmi() {
		return bmi;
	}

	public void setBmi(Double bmi) {
		this.bmi = bmi;
	}

	public String getInjectingDrugUse() {
		return injectingDrugUse;
	}

	public void setInjectingDrugUse(String injectingDrugUse) {
		this.injectingDrugUse = injectingDrugUse;
	}

	public String getHomeless() {
		return homeless;
	}

	public void setHomeless(String homeless) {
		this.homeless = homeless;
	}

	public String getImprisoned() {
		return imprisoned;
	}

	public void setImprisoned(String imprisoned) {
		this.imprisoned = imprisoned;
	}

	public String getSmoker() {
		return smoker;
	}

	public void setSmoker(String smoker) {
		this.smoker = smoker;
	}

	public String getDiabetic() {
		return diabetic;
	}

	public void setDiabetic(String diabetic) {
		this.diabetic = diabetic;
	}

	public String getHivStatus() {
		return hivStatus;
	}

	public void setHivStatus(String hivStatus) {
		this.hivStatus = hivStatus;
	}

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

}
