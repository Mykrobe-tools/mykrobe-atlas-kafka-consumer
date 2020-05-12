package com.makeandship.mykrobe.listeners.results;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface DistanceResultStreamBinding {
	String SOURCE_DISTANCE_RESULT = Constants.SOURCE_DISTANCE_RESULT;
	String SINK_CORE_TREE_DISTANCE_RESULT = Constants.SINK_CORE_TREE_DISTANCE_RESULT;
	String SINK_CORE_NEAREST_NEIGHBOUR_RESULT = Constants.SINK_CORE_NEAREST_NEIGHBOUR_RESULT;

	@Input(SOURCE_DISTANCE_RESULT)
	KStream<?, ?> inputDistanceResult();

	@Output(SINK_CORE_TREE_DISTANCE_RESULT)
	KStream<?, ?> outputCoreTreeDistanceResult();

	@Output(SINK_CORE_NEAREST_NEIGHBOUR_RESULT)
	KStream<?, ?> outputCoreNearestNeighbourResult();
}
