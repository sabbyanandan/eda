package com.example.notificationsink;

import java.util.Date;

public class UserNameChanged implements DomainEvent {

	public UserNameChanged() {
	}

	private String newNickName;

	private Date occuredAt;

	public UserNameChanged(String newNickName, Date occuredAt) {
		this.newNickName = newNickName;
		this.occuredAt = occuredAt;
	}

	public String getNewNickName() {
		return newNickName;
	}

	@Override
	public Date getOccuredAt() {
		return occuredAt;
	}
}
