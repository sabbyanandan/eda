package com.example.usersource;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {

	private String nickname = "";

	private final UUID uuid;

	private UserState state = UserState.INITIALIZED;

	private String region = "";

	private List<DomainEvent> changes = new ArrayList<>();

	public User(UUID uuid, String region) {
		this.uuid = uuid;
		this.region = region;
	}

	public UUID getUuid() {
		return uuid;
	}

	String getNickname() {
		return nickname;
	}

	public String getRegion() {
		return region;
	}

	public List<DomainEvent> getChanges() {
		return ImmutableList.copyOf(changes);
	}

	enum UserState {
		INITIALIZED, CREATED, ACTIVATED, DEACTIVATED
	}

	void create() { //behaviour
		if (isCreated()) { //invariant
			throw new IllegalStateException(); //NACK
		}
		//ACK
		userCreated(new UserCreated(getUuid(), getRegion(), new Date()));
	}

	private User userCreated(UserCreated userCreated) {
		state = UserState.CREATED; //state change
		changes.add(userCreated);
		return this;
	}

	void activate() {
		if (isActivated()) {
			throw new IllegalStateException();
		}
		userActivated(new UserActivated(new Date()));
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
		userDeactivated(new UserDeactivated(new Date()));
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

	boolean isCreated() {
		return state.equals(UserState.CREATED);
	}

	boolean isActivated() {
		return state.equals(UserState.ACTIVATED);
	}

	boolean isDeactivated() {
		return state.equals(UserState.DEACTIVATED);
	}
}
