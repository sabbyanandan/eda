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

	@Input("usersbyregion_input")
	KStream<?, ?> usersByRegionInput();

	@Output("usersbyregion_output")
	KStream<?, ?> usersByRegionOutput();

	@Output("localevents")
	MessageChannel localEvents();

	@Output("friendsnearby")
	MessageChannel friendsNearby();

	@Output("welcome")
	MessageChannel welcome();

}
