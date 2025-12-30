package com.plataformtrade.application.events;

import com.plataformtrade.domain.events.DomainEvent;

public interface DomainEventHandler<T extends DomainEvent> {
    Class<T> eventType();
    void handle(T event);
}
