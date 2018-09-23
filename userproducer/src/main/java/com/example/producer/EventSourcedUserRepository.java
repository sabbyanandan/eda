package com.example.producer;

import org.springframework.stereotype.Component;

@Component
public class EventSourcedUserRepository implements UserRepository {

	private final EventPublisher eventPublisher;

	public EventSourcedUserRepository(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void save(User user) {
		// Persist events as change logs
		user.getChanges().forEach(eventPublisher::sendEvent);
	}

	@Override
	public User load() {

		// Event sourcing!

		// Each new USER object has a stream of events, they are ordered by timestamp; if we replay the events, we would eventually
		// get to the current state of the object

		// you can recreate state from 1 month ago or a year ago!

		return null;
	}
}
