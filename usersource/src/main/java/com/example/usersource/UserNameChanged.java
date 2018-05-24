package com.example.usersource;

import java.time.Instant;

public class UserNameChanged implements DomainEvent {
    private final String newNickName;
    private final Instant when;

    public UserNameChanged(String newNickName, Instant when) {
        this.newNickName = newNickName;
        this.when = when;
    }

    public String getNewNickName() {
        return newNickName;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
