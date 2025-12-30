package com.plataformtrade.application.events;

import com.plataformtrade.domain.events.DomainEvent;

public interface EventDispatcher {
    void dispatch(DomainEvent event);
}
