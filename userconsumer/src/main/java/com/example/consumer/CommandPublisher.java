package com.example.consumer;

import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommandPublisher {

	@Publisher(channel = "welcome")
	public UUID sendWelcomeCommand(UUID uuid) {
		System.out.println("Welcome message sent for: " + uuid + "!");
		return uuid;
	}

	@Publisher(channel = "localevents")
	public String sendLocalEventsCommand(String command) {
		System.out.println("Trigger [" + command + "] command to initiate Local Events nearby service.");
		return command;
	}

	@Publisher(channel = "friendsnearby")
	public String sendFriendsNearbyCommand(String command) {
		System.out.println("Trigger [" + command + "] command to initiate with Friends nearby service.");
		return command;
	}
}
