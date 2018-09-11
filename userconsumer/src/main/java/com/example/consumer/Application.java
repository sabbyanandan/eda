package com.example.consumer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableBinding(UserChannels.class)
public class Application {

	public static final int WINDOW_SIZE = 30000;

	public static final String USERS_GROUPED_BY_REGION_SNAPSHOT = "users-grouped-by-region-snapshot";

	private final CommandPublisher commandPublisher;

	@Autowired
	private QueryableStoreRegistry queryableStoreRegistry;

	public Application(CommandPublisher commandPublisher) {
		this.commandPublisher = commandPublisher;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@StreamListener("users")
	public void commandHandler(DomainEvent event) {
		if (event instanceof UserCreated) {
			commandPublisher.sendWelcomeCommand(((UserCreated) event).getUuid());
		}
		else if (event instanceof UserActivated) {
			commandPublisher.sendLocalEventsCommand("localevents");
			commandPublisher.sendFriendsNearbyCommand("friendsnearby");
		}
		else if (event instanceof UserNameChanged) {
			// push notification
		}
		else if (event instanceof UserDeactivated) {
			// update audit service
			// notify report service
		}
	}

	@StreamListener("usersbyregion_input")
	@SendTo("usersbyregion_output")
	public KStream<Object, UsersByRegionCount> aggregateHandler(KStream<Object, DomainEvent> input) {

		// find no. of new users grouped by region and in the last 30s
		return input.filter((k, v) -> v instanceof UserCreated)
				.map((k, v) -> new KeyValue<>(((UserCreated) v).getRegion(), v))
				.groupByKey(Serialized
						.with(new Serdes.StringSerde(), new JsonSerde<>(DomainEvent.class)))
				.windowedBy(TimeWindows.of(WINDOW_SIZE))
				.count(Materialized.as(USERS_GROUPED_BY_REGION_SNAPSHOT))
				.toStream()
				.map((k, v) -> new KeyValue<>(null, new UsersByRegionCount(k.key(), v, k.window().start(), k.window().end())));
	}

	@RestController
	class InteractiveQueryController {

		@RequestMapping("/windows")
		public String windowedData(@RequestParam(value="region") String region) {

			ReadOnlyWindowStore<String, Long> queryableStore = queryableStoreRegistry.getQueryableStoreType(USERS_GROUPED_BY_REGION_SNAPSHOT,
					QueryableStoreTypes.windowStore());
			long now = System.currentTimeMillis();
			WindowStoreIterator<Long> regionCountIerator = queryableStore.fetch(region, now - (60 * 1000 * 5), now);

			StringJoiner regionCounts = new StringJoiner(", ", "[", "]");

			regionCountIerator.forEachRemaining(value -> regionCounts.add(String.valueOf(value.value)));

			return regionCounts.toString();
		}

		@RequestMapping("/windows/cumulative")
		public long windowedCumulative(@RequestParam(value="region") String region) {

			ReadOnlyWindowStore<String, Long> queryableStore = queryableStoreRegistry.getQueryableStoreType(USERS_GROUPED_BY_REGION_SNAPSHOT,
					QueryableStoreTypes.windowStore());
			long now = System.currentTimeMillis();
			WindowStoreIterator<Long> regionCountIerator = queryableStore.fetch(region, now - (60 * 1000 * 60), now);

			AtomicLong total = new AtomicLong(0);

			regionCountIerator.forEachRemaining(value -> total.addAndGet(value.value));

			return total.get();
		}

	}

}