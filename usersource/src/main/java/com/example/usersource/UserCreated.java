package com.example.usersource;

import java.util.Date;
import java.util.UUID;

public class UserCreated implements DomainEvent {

	public UserCreated() {
	}

	private UUID uuid;

	private String region = "";

	private Date occuredAt;

	public UserCreated(UUID uuid, String region, Date occuredAt) {
		this.uuid = uuid;
		this.region = region;
		this.occuredAt = occuredAt;
	}

	public UserCreated(Date occuredAt) {
		this.occuredAt = occuredAt;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getRegion() {
		return region;
	}

	@Override
	public Date getOccuredAt() {
		return occuredAt;
	}
}
