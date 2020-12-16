package com.makeandship.mykrobe.listeners.groups;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface GroupSearchStreamBinding {
	String SOURCE_GROUP_SEARCHES = Constants.SOURCE_GROUP_SEARCHES;
	String SINK_CORE_GROUP_SEARCHES = Constants.SINK_CORE_GROUP_SEARCHES;

	@Input(SOURCE_GROUP_SEARCHES)
	KStream<?, ?> inputGroupSearches();

	@Output(SINK_CORE_GROUP_SEARCHES)
	KStream<?, ?> outputCoreGroupSearches();
}
