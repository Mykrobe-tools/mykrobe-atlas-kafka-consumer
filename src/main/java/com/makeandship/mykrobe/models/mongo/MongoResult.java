package com.makeandship.mykrobe.models.mongo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoResult {
	@JsonProperty("received")
	private MongoDate received;
	@JsonProperty("reference")
	private String reference;
	@JsonProperty("completedBigsiQueries")
	private Integer completedBigsiQueries;
	@JsonProperty("totalBigsiQueries")
	private Integer totalBigsiQueries;
	@JsonProperty("results")
	private List<MongoSearchResult> results;

	public MongoDate getReceived() {
		return received;
	}

	public void setReceived(MongoDate received) {
		this.received = received;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getCompletedBigsiQueries() {
		return completedBigsiQueries;
	}

	public void setCompletedBigsiQueries(Integer completedBigsiQueries) {
		this.completedBigsiQueries = completedBigsiQueries;
	}

	public Integer getTotalBigsiQueries() {
		return totalBigsiQueries;
	}

	public void setTotalBigsiQueries(Integer totalBigsiQueries) {
		this.totalBigsiQueries = totalBigsiQueries;
	}

	public List<MongoSearchResult> getResults() {
		return results;
	}

	public void setResults(List<MongoSearchResult> results) {
		this.results = results;
	}

}
