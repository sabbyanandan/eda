package com.example.usersource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class EventPublisher {

	@Autowired
	Source foo;

	public DomainEvent sendEvent(DomainEvent event) {
		System.out.println("about to send: " + event);
		foo.output().send(MessageBuilder.createMessage(event,
				new MessageHeaders(Collections.singletonMap(MessageHeaders.CONTENT_TYPE, "application/json"))));
		return event;
	}
}
