package com.example.usersource;

import com.google.common.collect.ImmutableList;

import java.time.Instant;
import java.util.*;

public class User {

	private String nickname = "";

	private final UUID uuid;

	private UserState state = UserState.INITIALIZED;

	private List<DomainEvent> changes = new ArrayList<>();

	public User(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

	String getNickname() {
		return nickname;
	}

	public List<DomainEvent> getChanges() {
		return ImmutableList.copyOf(changes);
	}

	enum UserState {
		INITIALIZED, ACTIVATED, DEACTIVATED

	}

	void activate() { //behaviour
		if (isActivated()) { //invariant
			throw new IllegalStateException(); //NACK
		}
		//ACK
		userActivated(new UserActivated(Instant.now()));
	}

	private User userActivated(UserActivated userActivated) {
		state = UserState.ACTIVATED; //state change
		changes.add(userActivated);
		return this;
	}

	void deactivate() {
		if (isDeactivated()) {
			throw new IllegalStateException();
		}
		userDeactivated(new UserDeactivated(Instant.now()));
	}

	private User userDeactivated(UserDeactivated userDeactivated) {
		state = UserState.DEACTIVATED;
		changes.add(userDeactivated);
		return this;
	}

	void changeNicknameTo(String newNickName) {
		if (isDeactivated()) {
			throw new IllegalStateException();
		}
		userNameChanged(new UserNameChanged(newNickName, new Date()));
	}

	private User userNameChanged(UserNameChanged userNameChanged) {
		nickname = userNameChanged.getNewNickName();
		changes.add(userNameChanged);
		return this;
	}

	boolean isActivated() {
		return state.equals(UserState.ACTIVATED);
	}

	boolean isDeactivated() {
		return state.equals(UserState.DEACTIVATED);
	}
}
