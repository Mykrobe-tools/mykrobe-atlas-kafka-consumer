package com.makeandship.mykrobe.listeners.groups;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface GroupStreamBinding {
	String SOURCE_GROUPS = Constants.SOURCE_GROUPS;
	String SINK_CORE_GROUPS = Constants.SINK_CORE_GROUPS;

	@Input(SOURCE_GROUPS)
	KStream<?, ?> inputGroups();

	@Output(SINK_CORE_GROUPS)
	KStream<?, ?> outputCoreGroups();
}
