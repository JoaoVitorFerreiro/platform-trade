package com.plataformtrade.infra.events;

import com.plataformtrade.infra.persistence.entities.OutboxEventEntity;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@ConditionalOnProperty(prefix = "messaging.rabbit", name = "enabled", havingValue = "true")
public class RabbitEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public RabbitEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${messaging.rabbit.exchange:account.events}") String exchangeName,
            CircuitBreaker rabbitPublisherCircuitBreaker,
            Retry rabbitPublisherRetry
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.circuitBreaker = rabbitPublisherCircuitBreaker;
        this.retry = rabbitPublisherRetry;
    }

    public void publish(OutboxEventEntity event) {
        String routingKey = resolveRoutingKey(event.getEventType());
        Supplier<Void> supplier = () -> {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, event.getPayload());
            return null;
        };
        Supplier<Void> decorated = CircuitBreaker.decorateSupplier(circuitBreaker, supplier);
        decorated = Retry.decorateSupplier(retry, decorated);
        decorated.get();
    }

    private String resolveRoutingKey(String eventType) {
        if (eventType == null || eventType.isBlank()) {
            return "unknown.event";
        }
        return eventType;
    }
}
