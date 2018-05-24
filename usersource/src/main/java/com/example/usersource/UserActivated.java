package com.example.usersource;

import java.time.Instant;

public class UserActivated implements DomainEvent {
    private final Instant occuredAt;

    public UserActivated(Instant occuredAt) {
        this.occuredAt = occuredAt;
    }

    @Override
    public Instant getOccuredAt() {
        return occuredAt;
    }
}
