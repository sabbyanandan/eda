package com.example.usersource;

import java.time.Instant;

public class UserNameChanged implements DomainEvent {
    private final String newNickName;
    private final Instant occuredAt;

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
