package com.makeandship.mykrobe.listeners.experiment;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableBinding({ ExperimentStreamBinding.class })
public class ExperimentStreamProcessor {
	@SendTo({ ExperimentStreamBinding.SINK_CORE_EXPERIMENTS, ExperimentStreamBinding.SINK_CORE_METADATA_PATIENTS })
	@StreamListener(ExperimentStreamBinding.SOURCE_EXPERIMENTS)
	public KStream<Object, Object> processSourceExperiments(KStream<Object, Object> input) {
		log.trace(ExperimentStreamProcessor.class.getName() + "#processSourceExperiments: enter");

		return input.filter((key, value) -> true).map((key, value) -> buildRecord(value));
	}

	private KeyValue<Object, Object> buildRecord(Object message) {
		log.trace(ExperimentStreamProcessor.class.getName() + "#buildRecord: enter");

		Object key = null;
		Object value = null;

		return new KeyValue<>(key, value);
	}
}
