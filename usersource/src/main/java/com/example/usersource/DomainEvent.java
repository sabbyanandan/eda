package com.example.usersource;

import java.time.Instant;

public interface DomainEvent {
    Instant occuredAt();
}
