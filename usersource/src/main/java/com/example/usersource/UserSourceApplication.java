package com.example.usersource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;
import java.util.UUID;

@EnableScheduling
@EnableBinding(Source.class)
@SpringBootApplication
public class UserSourceApplication {

	private final UserRepository userRepository;

	public UserSourceApplication(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserSourceApplication.class, args);
	}

	@Scheduled(fixedRate = 2000L)
	public void randomUsers() throws InterruptedException {
		User user = new User(UUID.randomUUID());
		user.activate();
		Thread.sleep(1200);
		user.changeNicknameTo("Name" + new Random().nextInt(10));
		Thread.sleep(1900);
		user.deactivate();
		userRepository.save(user);
	}
}
