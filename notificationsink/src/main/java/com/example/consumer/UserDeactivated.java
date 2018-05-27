package com.example.consumer;

import java.util.Date;

public class UserDeactivated implements DomainEvent {

	public UserDeactivated() {
	}

	private Date occuredAt;

	public UserDeactivated(Date occuredAt) {
		this.occuredAt = occuredAt;
	}

	@Override
	public Date getOccuredAt() {
		return occuredAt;
	}
}
