package com.example.producer;

import java.util.Date;

public class UserNameChanged implements DomainEvent {

	public UserNameChanged() {
	}

	private String newName;

	private Date occuredAt;

	public UserNameChanged(String newName, Date occuredAt) {
		this.newName = newName;
		this.occuredAt = occuredAt;
	}

	public String getNewName() {
		return newName;
	}

	@Override
	public Date getOccuredAt() {
		return occuredAt;
	}
}
