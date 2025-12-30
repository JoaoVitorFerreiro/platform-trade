package com.plataformtrade.domain.events;

import java.time.Instant;

public interface DomainEvent {
    String getEventId();
    String getAggregateId();
    String getType();
    Instant getOccurredOn();
}
