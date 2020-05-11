package com.makeandship.mykrobe.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makeandship.mykrobe.models.mongo.MongoDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperimentResult {
	@JsonProperty("type")
	private String type;
	@JsonProperty("received")
	private MongoDate received;
	@JsonProperty("kmer")
	private Integer kmer;
	@JsonProperty("files")
	private List<String> files;
	@JsonProperty("version")
	private MykrobeVersion version;
	@JsonProperty("genotypeModel")
	private String genotypeModel;
	@JsonProperty("externalId")
	private String externalId;
	@JsonProperty("r")
	private boolean r;
	@JsonProperty("mdr")
	private boolean mdr;
	@JsonProperty("xdr")
	private boolean xdr;
	@JsonProperty("tdr")
	private boolean tdr;
	@JsonProperty("susceptibility")
	private List<MykrobeSusceptibility> susceptibility;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MongoDate getReceived() {
		return received;
	}

	public void setReceived(MongoDate received) {
		this.received = received;
	}

	public Integer getKmer() {
		return kmer;
	}

	public void setKmer(Integer kmer) {
		this.kmer = kmer;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public MykrobeVersion getVersion() {
		return version;
	}

	public void setVersion(MykrobeVersion version) {
		this.version = version;
	}

	public String getGenotypeModel() {
		return genotypeModel;
	}

	public void setGenotypeModel(String genotypeModel) {
		this.genotypeModel = genotypeModel;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public boolean isR() {
		return r;
	}

	public void setR(boolean r) {
		this.r = r;
	}

	public boolean isMdr() {
		return mdr;
	}

	public void setMdr(boolean mdr) {
		this.mdr = mdr;
	}

	public boolean isXdr() {
		return xdr;
	}

	public void setXdr(boolean xdr) {
		this.xdr = xdr;
	}

	public boolean isTdr() {
		return tdr;
	}

	public void setTdr(boolean tdr) {
		this.tdr = tdr;
	}

	public List<MykrobeSusceptibility> getSusceptibility() {
		return susceptibility;
	}

	public void setSusceptibility(List<MykrobeSusceptibility> susceptibility) {
		this.susceptibility = susceptibility;
	}
}
