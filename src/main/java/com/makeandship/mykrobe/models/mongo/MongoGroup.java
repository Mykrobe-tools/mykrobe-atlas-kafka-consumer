package com.makeandship.mykrobe.models.mongo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoGroup {
	@JsonProperty("_id")
	private MongoId id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("annotation")
	private String annotation;
	@JsonProperty("searches")
	private List<MongoId> searches;
	@JsonProperty("experiments")
	private List<MongoId> experiments;

	public MongoId getId() {
		return id;
	}

	public void setId(MongoId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public List<MongoId> getSearches() {
		return searches;
	}

	public void setSearches(List<MongoId> searches) {
		this.searches = searches;
	}

	public List<MongoId> getExperiments() {
		return experiments;
	}

	public void setExperiments(List<MongoId> experiments) {
		this.experiments = experiments;
	}

}
