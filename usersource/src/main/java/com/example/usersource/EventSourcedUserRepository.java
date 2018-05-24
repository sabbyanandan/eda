package com.example.usersource;

import org.springframework.stereotype.Component;

@Component
public class EventSourcedUserRepository implements UserRepository {

	private final EventPublisher eventPublisher;

	public EventSourcedUserRepository(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void save(User user) {
		user.getChanges().forEach(eventPublisher::sendEvent);
	}
}
