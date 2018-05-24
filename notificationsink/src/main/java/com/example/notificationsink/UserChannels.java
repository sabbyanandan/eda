package com.example.notificationsink;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by sanandan on 5/24/18.
 */
public interface UserChannels {

	@Input("users")
	SubscribableChannel users();

	@Input("useraggregates")
	KStream<?, ?> useraggregates();

	@Output("localevents")
	MessageChannel localevents();

	@Output("friendsnearby")
	MessageChannel friendsnearby();

	@Output("welcome")
	MessageChannel welcome();

}
