package com.makeandship.mykrobe.listeners.results;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface PredictorResultStreamBinding {
	String SOURCE_PREDICTOR_RESULT = Constants.SOURCE_PREDICTOR_RESULT;
	String SINK_CORE_PREDICTOR_RESULT = Constants.SINK_CORE_PREDICTOR_RESULT;

	@Input(SOURCE_PREDICTOR_RESULT)
	KStream<?, ?> inputPredictorResult();

	@Output(SINK_CORE_PREDICTOR_RESULT)
	KStream<?, ?> outputCorePredictorResult();
}
