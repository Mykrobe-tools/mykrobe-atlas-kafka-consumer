package com.makeandship.mykrobe.listeners.groups;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface GroupExperimentStreamBinding {
	String SOURCE_GROUP_EXPERIMENTS = Constants.SOURCE_GROUP_EXPERIMENTS;
	String SINK_CORE_GROUP_EXPERIMENTS = Constants.SINK_CORE_GROUP_EXPERIMENTS;

	@Input(SOURCE_GROUP_EXPERIMENTS)
	KStream<?, ?> inputGroupExperiments();

	@Output(SINK_CORE_GROUP_EXPERIMENTS)
	KStream<?, ?> outputCoreGroupExperiments();
}
