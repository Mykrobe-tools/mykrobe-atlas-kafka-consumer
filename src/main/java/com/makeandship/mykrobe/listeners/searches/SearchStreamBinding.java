package com.makeandship.mykrobe.listeners.searches;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface SearchStreamBinding {
	String SOURCE_SEARCHES = Constants.SOURCE_SEARCHES;
	String SINK_CORE_SEARCHES = Constants.SINK_CORE_SEARCHES;

	@Input(SOURCE_SEARCHES)
	KStream<?, ?> inputSearches();

	@Output(SINK_CORE_SEARCHES)
	KStream<?, ?> outputCoreSearches();
}
