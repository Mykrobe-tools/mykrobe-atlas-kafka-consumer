package com.makeandship.mykrobe.listeners.results;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.makeandship.mykrobe.Constants;

public interface PredictorResultLineageStreamBinding {
	String SOURCE_PREDICTOR_RESULT_LINEAGE = Constants.SOURCE_PREDICTOR_RESULT_LINEAGE;
	String SINK_CORE_PREDICTOR_RESULT_LINEAGE = Constants.SINK_CORE_PREDICTOR_RESULT_LINEAGE;

	@Input(SOURCE_PREDICTOR_RESULT_LINEAGE)
	KStream<?, ?> inputPredictorResultLineage();

	@Output(SINK_CORE_PREDICTOR_RESULT_LINEAGE)
	KStream<?, ?> outputCorePredictorResultLineage();
}
