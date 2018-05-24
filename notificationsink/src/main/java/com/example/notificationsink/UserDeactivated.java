package com.example.notificationsink;

import java.time.Instant;

public class UserDeactivated implements DomainEvent {
    public UserDeactivated() {
    }

    private  Instant occuredAt;

    public UserDeactivated(Instant occuredAt) {
        this.occuredAt = occuredAt;
    }

    @Override
    public Instant getOccuredAt() {
        return occuredAt;
    }
}
