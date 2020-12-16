package com.makeandship.mykrobe.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoSearch {
	@JsonProperty("_id")
	private MongoId id;
	@JsonProperty("bigsi")
	private MongoBigsi bigsi;
	@JsonProperty("type")
	private String type;
	@JsonProperty("expires")
	private MongoDate expires;
	@JsonProperty("status")
	private String status;
	@JsonProperty("created")
	private MongoDate created;
	@JsonProperty("modified")
	private MongoDate modified;
	@JsonProperty("result")
	private MongoResult result;

	public MongoId getId() {
		return id;
	}

	public void setId(MongoId id) {
		this.id = id;
	}

	public MongoBigsi getBigsi() {
		return bigsi;
	}

	public void setBigsi(MongoBigsi bigsi) {
		this.bigsi = bigsi;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MongoDate getExpires() {
		return expires;
	}

	public void setExpires(MongoDate expires) {
		this.expires = expires;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public MongoResult getResult() {
		return result;
	}

	public void setResult(MongoResult result) {
		this.result = result;
	}

}
