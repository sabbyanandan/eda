package com.example.notificationsink;

import java.time.Instant;

public interface DomainEvent {
    Instant getOccuredAt();
}
