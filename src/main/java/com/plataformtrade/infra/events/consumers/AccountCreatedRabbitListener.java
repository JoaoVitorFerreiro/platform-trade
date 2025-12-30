package com.plataformtrade.infra.events.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "messaging.rabbit", name = "enabled", havingValue = "true")
public class AccountCreatedRabbitListener {
    private static final Logger logger = LoggerFactory.getLogger(AccountCreatedRabbitListener.class);

    @RabbitListener(queues = "${messaging.rabbit.queues.account-created:account.created}")
    public void handle(String payload) {
        logger.info("Received account.created event from RabbitMQ: payload={}", payload);
    }
}
