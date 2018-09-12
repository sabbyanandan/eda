package com.example.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.config.EnablePublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@EnableScheduling
@EnablePublisher
@EnableBinding(Source.class)
@SpringBootApplication
public class Application {

	private final UserRepository userRepository;

	public Application(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	static List<String> regions = Arrays
			.asList("US-CA", "US-NY", "US-PA", "US-IL", "US-IL", "US-CA", "US-NY", "US-HI", "US-CA", "US-NY", "US-CA",
					"US-CA", "US-CA", "US-NY", "US-IL");

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Scheduled(fixedRate = 500L)
	public void randomUsers() throws InterruptedException {

		User user = new User(UUID.randomUUID(), regions.get(new Random().nextInt(regions.size())));

		// event-1: create
		user.create();

		// wait for a random sleep between 100ms -> 300ms
		Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300 + 1));

		// event-2: activate
		user.activate();

		// wait for a random sleep between 100ms -> 300ms
		Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300 + 1));

		// event-3: change name
		user.changeNameTo("Name - " + new Random().nextInt(100));

		// Thread.sleep(1900);

		// event-4: deactivate
		// user.deactivate();

		userRepository.save(user);
	}
}
