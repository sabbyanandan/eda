package com.example.notificationsink;

import java.time.Instant;

public class UserActivated implements DomainEvent {

	public UserActivated() {
	}

	private Instant occuredAt;

	public UserActivated(Instant occuredAt) {
		this.occuredAt = occuredAt;
	}

	@Override
	public Instant getOccuredAt() {
		return occuredAt;
	}
}
