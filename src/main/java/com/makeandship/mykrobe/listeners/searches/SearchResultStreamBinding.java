package com.makeandship.mykrobe.listeners.searches;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface SearchResultStreamBinding {
	String SOURCE_SEARCH_RESULTS = Constants.SOURCE_SEARCH_RESULTS;
	String SINK_CORE_SEARCH_RESULTS = Constants.SINK_CORE_SEARCH_RESULTS;

	@Input(SOURCE_SEARCH_RESULTS)
	KStream<?, ?> inputSearchResults();

	@Output(SINK_CORE_SEARCH_RESULTS)
	KStream<?, ?> outputCoreSearchResults();
}
