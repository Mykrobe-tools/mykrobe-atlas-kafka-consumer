package com.makeandship.mykrobe.models.mongo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makeandship.mykrobe.models.ExperimentMetadata;
import com.makeandship.mykrobe.models.ExperimentResult;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoExperiment {
	@JsonProperty("_id")
	private MongoId id;
	@JsonProperty("owner")
	private MongoId owner;
	@JsonProperty("created")
	private MongoDate created;
	@JsonProperty("modified")
	private MongoDate modified;
	@JsonProperty("file")
	private String file;
	@JsonProperty("metadata")
	private ExperimentMetadata metadata;
	@JsonProperty("results")
	private List<ExperimentResult> results;

	public MongoId getId() {
		return id;
	}

	public void setId(MongoId id) {
		this.id = id;
	}

	public MongoId getOwner() {
		return owner;
	}

	public void setOwner(MongoId owner) {
		this.owner = owner;
	}

	public MongoDate getCreated() {
		return created;
	}

	public void setCreated(MongoDate created) {
		this.created = created;
	}

	public MongoDate getModified() {
		return modified;
	}

	public void setModified(MongoDate modified) {
		this.modified = modified;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public ExperimentMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(ExperimentMetadata metadata) {
		this.metadata = metadata;
	}

	public List<ExperimentResult> getResults() {
		return results;
	}

	public void setResults(List<ExperimentResult> results) {
		this.results = results;
	}

}
