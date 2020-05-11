package com.makeandship.mykrobe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MykrobeVersion {
	@JsonProperty("mykrobe-predictor")
	private String mykrobePredictor;
	@JsonProperty("mykrobe-atlas")
	private String mykrobeAtlas;

	public String getMykrobePredictor() {
		return mykrobePredictor;
	}

	public void setMykrobePredictor(String mykrobePredictor) {
		this.mykrobePredictor = mykrobePredictor;
	}

	public String getMykrobeAtlas() {
		return mykrobeAtlas;
	}

	public void setMykrobeAtlas(String mykrobeAtlas) {
		this.mykrobeAtlas = mykrobeAtlas;
	}
}
