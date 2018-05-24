package com.example.notificationsink;

import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Component;

@Component
public class CommandPublisher {

	@Publisher(channel = "welcome")
	public String sendWelcomeCommand(String command) {
		System.out.println("welcome mssage sent!");
		return command;
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
