package com.example.consumer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
@EnableBinding(UserChannels.class)
public class Application {

	private final CommandPublisher commandPublisher;

	public Application(CommandPublisher commandPublisher) {
		this.commandPublisher = commandPublisher;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@StreamListener("users")
	public void commandHandler(DomainEvent event) {
		//		System.out.println(event);

		if (event instanceof UserCreated) {
			commandPublisher.sendWelcomeCommand(((UserCreated) event).getUuid());
		}
		else if (event instanceof UserActivated) {
			// commandPublisher.sendLocalEventsCommand("localevents");
			// commandPublisher.sendFriendsNearbyCommand("friendsnearby");
		}
		else if (event instanceof UserNameChanged) {
			// more commands
		}
		else if (event instanceof UserDeactivated) {
			// more commands
			// update audit service
			// notiy report service
		}
	}

	@StreamListener("usersbyregion_input")
	@SendTo("usersbyregion_output")
	public KStream<Object, UsersByRegionCount> aggregateHandler(KStream<Object, DomainEvent> input) {

		// find no. of new users created in the last 30s by region
		return input.filter((k, v) -> v instanceof UserCreated)
				.map((k, v) -> new KeyValue<>(((UserCreated) v).getRegion(), v))
				.groupByKey(Serialized
						.with(new Serdes.StringSerde(), new JsonSerde<>(DomainEvent.class)))
				.windowedBy(TimeWindows.of(30000))
				.count(Materialized.as("users-group-by-region"))
				.toStream()
				.map((k, v) -> {
					System.out
							.println(
									"In the last 30 secs, " + v + " new Users were CREATED in the " + k + " Region.");
					return new KeyValue(null, new UsersByRegionCount(k.key(), v, k.window().start(), k.window().end()));
				});
	}
}