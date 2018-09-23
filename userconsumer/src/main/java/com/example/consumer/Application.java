package com.example.consumer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableBinding(UserChannels.class)
public class Application {

	public static final int WINDOW_SIZE = 30000;

	public static final String USERS_GROUPED_BY_REGION_SNAPSHOT = "users-grouped-by-region-snapshot";

	private final CommandPublisher commandPublisher;

	@Autowired
	private InteractiveQueryService interactiveQueryService;

	public Application(CommandPublisher commandPublisher) {
		this.commandPublisher = commandPublisher;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
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
				.map((k, v) -> new KeyValue<>(null,
						new UsersByRegionCount(k.key(), v, k.window().start(), k.window().end())));
	}

	@RestController
	class InteractiveQueryController {

		@RequestMapping("/windows")
		public List<UsersByRegionCount> windowedData() {

			ReadOnlyWindowStore<String, Long> queryableStore = interactiveQueryService
					.getQueryableStore(USERS_GROUPED_BY_REGION_SNAPSHOT,
							QueryableStoreTypes.windowStore());

			List<UsersByRegionCount> usersByRegionCounts = new ArrayList<>();

			if (queryableStore != null) {
				long now = System.currentTimeMillis();
				KeyValueIterator<Windowed<String>, Long> regionCountIterator = queryableStore
						.fetch("US-CA", "US-PA", now - (60 * 1000 * 2), now);

				//Remove any duplicate windows for the purposes of UI
				Set<KeyValue<Windowed<String>, Long>> windowedSet = new LinkedHashSet<>();
				regionCountIterator.forEachRemaining(windowedSet::add);
				regionCountIterator.close();

				//Transform windows to a list of domain objects
				windowedSet.forEach(value -> usersByRegionCounts.add(new UsersByRegionCount(value.key.key(),
						value.value, value.key.window().start(), value.key.window().end())));
			}
			return usersByRegionCounts;
		}

		@RequestMapping("/windows/cumulative")
		public long windowedCumulative(@RequestParam(value = "region") String region) {

			ReadOnlyWindowStore<String, Long> queryableStore = interactiveQueryService
					.getQueryableStore(USERS_GROUPED_BY_REGION_SNAPSHOT,
							QueryableStoreTypes.windowStore());
			AtomicLong total = new AtomicLong(0);
			if (queryableStore != null) {
				long now = System.currentTimeMillis();
				WindowStoreIterator<Long> regionCountIerator = queryableStore
						.fetch(region, now - (60 * 1000 * 60), now);
				regionCountIerator.forEachRemaining(value -> total.addAndGet(value.value));
			}
			return total.get();
		}

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

}