package com.plataformtrade.infra.events;

import com.plataformtrade.application.events.DomainEventHandler;
import com.plataformtrade.application.events.EventDispatcher;
import com.plataformtrade.domain.events.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleEventDispatcher implements EventDispatcher {
    private final List<DomainEventHandler<?>> handlers;

    public SimpleEventDispatcher(List<DomainEventHandler<?>> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void dispatch(DomainEvent event) {
        for (DomainEventHandler<?> handler : handlers) {
            if (handler.eventType().isAssignableFrom(event.getClass())) {
                dispatchToHandler(handler, event);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends DomainEvent> void dispatchToHandler(DomainEventHandler<T> handler, DomainEvent event) {
        handler.handle((T) event);
    }
}
