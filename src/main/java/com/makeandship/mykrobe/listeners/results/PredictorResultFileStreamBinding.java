package com.makeandship.mykrobe.listeners.results;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface PredictorResultFileStreamBinding {
	String SOURCE_PREDICTOR_RESULT_FILE = Constants.SOURCE_PREDICTOR_RESULT_FILE;
	String SINK_CORE_PREDICTOR_RESULT_FILE = Constants.SINK_CORE_PREDICTOR_RESULT_FILE;

	@Input(SOURCE_PREDICTOR_RESULT_FILE)
	KStream<?, ?> inputPredictorResultFile();

	@Output(SINK_CORE_PREDICTOR_RESULT_FILE)
	KStream<?, ?> outputCorePredictorResultFile();
}
