package com.plataformtrade.infra.events;

import com.plataformtrade.infra.persistence.entities.OutboxEventEntity;
import com.plataformtrade.infra.persistence.repositories.OutboxEventJpaRepository;
import com.plataformtrade.infra.observability.CustomMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "messaging.rabbit", name = "enabled", havingValue = "true")
public class OutboxEventProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OutboxEventProcessor.class);
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_SENT = "SENT";
    private static final String STATUS_FAILED = "FAILED";

    private final OutboxEventJpaRepository outboxEventRepository;
    private final RabbitEventPublisher rabbitEventPublisher;
    private final CustomMetrics customMetrics;

    public OutboxEventProcessor(
            OutboxEventJpaRepository outboxEventRepository,
            RabbitEventPublisher rabbitEventPublisher,
            CustomMetrics customMetrics
    ) {
        this.outboxEventRepository = outboxEventRepository;
        this.rabbitEventPublisher = rabbitEventPublisher;
        this.customMetrics = customMetrics;
    }

    @Scheduled(fixedDelayString = "${messaging.outbox.poll-interval-ms:5000}")
    @Transactional
    public void processPendingEvents() {
        List<OutboxEventEntity> pendingEvents =
                outboxEventRepository.findTop100ByStatusOrderByOccurredOnAsc(STATUS_PENDING);

        for (OutboxEventEntity event : pendingEvents) {
            try {
                rabbitEventPublisher.publish(event);
                event.setStatus(STATUS_SENT);
                event.setSentAt(Instant.now());
                event.setErrorMessage(null);
                customMetrics.incrementOutboxPublished();
            } catch (Exception ex) {
                logger.error("Failed to publish event to RabbitMQ: eventId={}", event.getEventId(), ex);
                event.setStatus(STATUS_FAILED);
                event.setErrorMessage(trimError(ex.getMessage()));
                customMetrics.incrementOutboxFailed();
            }
        }
    }

    private String trimError(String message) {
        if (message == null) {
            return null;
        }
        return message.length() <= 500 ? message : message.substring(0, 500);
    }
}
