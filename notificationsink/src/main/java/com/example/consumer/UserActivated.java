package com.example.consumer;

import java.util.Date;

public class UserActivated implements DomainEvent {

    public UserActivated() {
    }

    private Date occuredAt;

    public UserActivated(Date occuredAt) {
        this.occuredAt = occuredAt;
    }

    @Override
    public Date getOccuredAt() {
        return occuredAt;
    }
}
