package com.example.producer;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {

	private String name = "";

	private String region = "";

	private final UUID uuid;

	private UserState state = UserState.INITIALIZED;

	private List<DomainEvent> changes = new ArrayList<>();

	public User(UUID uuid, String region) {
		this.uuid = uuid;
		this.region = region;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
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

	void create() { //behavior
		if (isCreated()) {
			throw new IllegalStateException();
		}

		userCreated(new UserCreated(getUuid(), getRegion(), new Date()));
	}

	private User userCreated(UserCreated userCreated) {
		state = UserState.CREATED; //state transition
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
		state = UserState.ACTIVATED; //state transition
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
		state = UserState.DEACTIVATED; //state transition
		changes.add(userDeactivated);
		return this;
	}

	void changeNameTo(String name) {
		if (isDeactivated()) {
			throw new IllegalStateException();
		}
		userNameChanged(new UserNameChanged(name, new Date()));
	}

	private User userNameChanged(UserNameChanged userNameChanged) {
		name = userNameChanged.getNewName();
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
