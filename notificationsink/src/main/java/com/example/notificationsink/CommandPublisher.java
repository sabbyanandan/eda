package com.example.notificationsink;

import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommandPublisher {

	@Publisher(channel = "welcome")
	public UUID sendWelcomeCommand(UUID uuid) {
		System.out.println("welcome message sent for: " + uuid + "!");
		return uuid;
	}

	@Publisher(channel = "localevents")
	public String sendLocalEventsCommand(String command) {
		System.out.println("about to sendLocalEventsCommand: " + command);
		return command;
	}

	@Publisher(channel = "friendsnearby")
	public String sendFriendsNearbyCommand(String command) {
		System.out.println("about to sendFriendsNearbyCommand: " + command);
		return command;
	}
}
