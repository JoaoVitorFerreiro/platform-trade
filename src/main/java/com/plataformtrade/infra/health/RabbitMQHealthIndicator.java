package com.plataformtrade.infra.health;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "messaging.rabbit", name = "enabled", havingValue = "true")
public class RabbitMQHealthIndicator implements HealthIndicator {
    private final ConnectionFactory connectionFactory;

    public RabbitMQHealthIndicator(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Health health() {
        try (var connection = connectionFactory.createConnection()) {
            if (connection != null && connection.isOpen()) {
                return Health.up().withDetail("rabbitmq", "reachable").build();
            }
            return Health.down().withDetail("rabbitmq", "connection closed").build();
        } catch (Exception ex) {
            return Health.down(ex).build();
        }
    }
}
