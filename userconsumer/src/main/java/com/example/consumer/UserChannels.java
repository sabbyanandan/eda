package com.example.consumer;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface UserChannels {

	@Input("usersbyregion_input")
	KStream<?, ?> usersByRegionInput();

	@Output("usersbyregion_output")
	KStream<?, ?> usersByRegionOutput();

}
