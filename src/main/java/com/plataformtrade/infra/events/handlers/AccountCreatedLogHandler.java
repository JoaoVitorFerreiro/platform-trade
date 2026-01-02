package com.plataformtrade.infra.events.handlers;

import com.plataformtrade.application.events.DomainEventHandler;
import com.plataformtrade.domain.events.AccountCreatedEvent;
import com.plataformtrade.infra.observability.CustomMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountCreatedLogHandler implements DomainEventHandler<AccountCreatedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(AccountCreatedLogHandler.class);
    private final CustomMetrics customMetrics;

    public AccountCreatedLogHandler(CustomMetrics customMetrics) {
        this.customMetrics = customMetrics;
    }

    @Override
    public Class<AccountCreatedEvent> eventType() {
        return AccountCreatedEvent.class;
    }

    @Override
    public void handle(AccountCreatedEvent event) {
        logger.info("Account created: accountId={}, email={}", event.getAggregateId(), event.getEmail());
        customMetrics.incrementAccountCreated();
    }
}
