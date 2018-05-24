package com.example.notificationsink;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;

@SpringBootApplication
@EnableBinding(UserChannels.class)
public class NotificationSinkApplication {

	private final CommandPublisher commandPublisher;

	public NotificationSinkApplication(CommandPublisher commandPublisher) {
		this.commandPublisher = commandPublisher;
	}

	public static void main(String[] args) {
		SpringApplication.run(NotificationSinkApplication.class, args);
	}

	@StreamListener("users")
	public void commandHandler(DomainEvent event) {
		//		System.out.println(event);

		if (event instanceof UserCreated) {
			commandPublisher.sendWelcomeCommand("welcome");
		}
		else if (event instanceof UserActivated) {
			//			commandPublisher.sendLocalEventsCommand("localevents");
			//			commandPublisher.sendFriendsNearbyCommand("friendsnearby");
		}
		else if (event instanceof UserNameChanged) {
			// more commands
		}
		else if (event instanceof UserDeactivated) {
			// more commands
		}
	}

	@StreamListener("useraggregates")
	public void aggregateHandler(KStream<Object, DomainEvent> input) {

		// find no. of users activated in the last 10s for the state of California

		Serde<DomainEvent> domainEventSerde = new JsonSerde<>(DomainEvent.class);

		input.filter((k, v) -> v instanceof UserCreated)
				.map((k, v) -> {
					System.out.println(
							"UUID = " + ((UserCreated) v).getUuid() + " | Region = " + ((UserCreated) v).getRegion());
					return new KeyValue(k, v);
				})
				.filter((k, v) -> "California/US".equals(((UserCreated) v).getRegion()))
				.map((k, v) -> {
					System.out.println("It is California!");
					return new KeyValue(v, v);
				})
				.groupBy((k, v) -> new KeyValue(((UserCreated) v).getRegion(), v), Serialized
						.with(new Serdes.StringSerde(), domainEventSerde))
				.windowedBy(TimeWindows.of(1000))
				.count()
				.toStream()
				.map((k, v) -> {
					System.out.println("SALSA Key = " + k + " | Value = " + v);
					return new KeyValue(k, v);
				});

		//		input.filter((k, v) -> v instanceof UserCreated)
		//				.map((k, v) -> {
		//					System.out.println("from kstream " + ((UserCreated) v).getUuid());
		//					return new KeyValue(k, v);
		//				});
	}
}
