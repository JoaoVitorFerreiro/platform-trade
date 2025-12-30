package com.plataformtrade.infra.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataformtrade.application.events.DomainEventPublisher;
import com.plataformtrade.application.events.EventDispatcher;
import com.plataformtrade.domain.events.DomainEvent;
import com.plataformtrade.infra.persistence.entities.OutboxEventEntity;
import com.plataformtrade.infra.persistence.repositories.OutboxEventJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxEventPublisher implements DomainEventPublisher {
    private static final String STATUS_PENDING = "PENDING";
    private final OutboxEventJpaRepository outboxEventRepository;
    private final EventDispatcher eventDispatcher;
    private final ObjectMapper objectMapper;

    public OutboxEventPublisher(
            OutboxEventJpaRepository outboxEventRepository,
            EventDispatcher eventDispatcher,
            ObjectMapper objectMapper
    ) {
        this.outboxEventRepository = outboxEventRepository;
        this.eventDispatcher = eventDispatcher;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void publish(DomainEvent event) {
        outboxEventRepository.save(new OutboxEventEntity(
                event.getEventId(),
                event.getAggregateId(),
                event.getType(),
                toPayload(event),
                event.getOccurredOn(),
                STATUS_PENDING
        ));
        eventDispatcher.dispatch(event);
    }

    private String toPayload(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize event payload", ex);
        }
    }
}
