package com.example.usersource;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

	@Publisher(channel = Source.OUTPUT)
	public DomainEvent sendEvent(DomainEvent event) {
		System.out.println("about to sendEvent: " + event);
		return event;
	}
}
