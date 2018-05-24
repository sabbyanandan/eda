package com.example.usersource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@EnableScheduling
@EnableBinding(Source.class)
@SpringBootApplication
public class UserSourceApplication {

	private final UserRepository userRepository;

	public UserSourceApplication(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	static List<String> regions = Arrays
			.asList("California/US", "NewYork/US", "Pennsylvania/US", "Illinois/US", "Hawaii/US");

	public static void main(String[] args) {
		SpringApplication.run(UserSourceApplication.class, args);
	}

	@Scheduled(fixedRate = 2000L)
	public void randomUsers() throws InterruptedException {

		User user = new User(UUID.randomUUID(), "California/US"); //regions.get(new Random().nextInt(regions.size())));

		// create
		user.create();

		// wait for a random sleep between 100ms -> 300ms
		Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300 + 1));

		// activate
		user.activate();

		// wait for a random sleep between 1s -> 3s
		Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000 + 1));

		user.changeNicknameTo("Name - " + new Random().nextInt(100));

		//		Thread.sleep(1900);
		//		user.deactivate();

		userRepository.save(user);
	}
}
