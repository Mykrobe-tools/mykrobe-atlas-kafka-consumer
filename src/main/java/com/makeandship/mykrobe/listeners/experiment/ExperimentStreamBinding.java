package com.makeandship.mykrobe.listeners.experiment;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface ExperimentStreamBinding {
	String SOURCE_EXPERIMENTS = Constants.SOURCE_EXPERIMENTS;
	String SINK_CORE_EXPERIMENTS = Constants.SINK_CORE_EXPERIMENTS;

	@Input(SOURCE_EXPERIMENTS)
	KStream<?, ?> inputExperiments();

	@Output(SINK_CORE_EXPERIMENTS)
	KStream<?, ?> outputCoreExperiments();
}
