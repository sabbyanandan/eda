package com.example.notificationsink;

import java.time.Instant;

public class UserNameChanged implements DomainEvent {

	public UserNameChanged() {
	}

	private String newNickName;

	private Instant occuredAt;

	public UserNameChanged(String newNickName, Instant occuredAt) {
		this.newNickName = newNickName;
		this.occuredAt = occuredAt;
	}

	public String getNewNickName() {
		return newNickName;
	}

	@Override
	public Instant getOccuredAt() {
		return occuredAt;
	}
}
