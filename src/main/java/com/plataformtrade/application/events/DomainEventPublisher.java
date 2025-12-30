package com.plataformtrade.application.events;

import com.plataformtrade.domain.events.DomainEvent;

import java.util.List;

public interface DomainEventPublisher {
    void publish(DomainEvent event);

    default void publishAll(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            publish(event);
        }
    }
}
