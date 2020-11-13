package com.makeandship.mykrobe.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MykrobePhylogenetics {
	@JsonProperty("type")
	private String type;
	@JsonProperty("result")
	private String result;
	@JsonProperty("percentCoverage")
	private float percentCoverage;
	@JsonProperty("medianDepth")
	private int medianDepth;
	@JsonProperty("lineage")
	private List<String> lineages;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the percentCoverage
	 */
	public float getPercentCoverage() {
		return percentCoverage;
	}

	/**
	 * @param percentCoverage the percentCoverage to set
	 */
	public void setPercentCoverage(float percentCoverage) {
		this.percentCoverage = percentCoverage;
	}

	/**
	 * @return the medianDepth
	 */
	public int getMedianDepth() {
		return medianDepth;
	}

	/**
	 * @param medianDepth the medianDepth to set
	 */
	public void setMedianDepth(int medianDepth) {
		this.medianDepth = medianDepth;
	}

	public List<String> getLineages() {
		return lineages;
	}

	public void setLineages(List<String> lineages) {
		this.lineages = lineages;
	}
}
