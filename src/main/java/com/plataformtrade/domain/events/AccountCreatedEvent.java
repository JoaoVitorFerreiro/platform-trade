package com.plataformtrade.domain.events;

import java.time.Instant;
import java.util.UUID;

public class AccountCreatedEvent implements DomainEvent {
    private final String eventId;
    private final String aggregateId;
    private final String name;
    private final String email;
    private final String document;
    private final Instant occurredOn;

    public AccountCreatedEvent(String aggregateId, String name, String email, String document) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.name = name;
        this.email = email;
        this.document = document;
        this.occurredOn = Instant.now();
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getType() {
        return "account.created";
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDocument() {
        return document;
    }
}
