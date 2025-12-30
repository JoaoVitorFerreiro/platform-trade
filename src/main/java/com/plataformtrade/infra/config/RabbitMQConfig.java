package com.plataformtrade.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "messaging.rabbit", name = "enabled", havingValue = "true")
public class RabbitMQConfig {
    public static final String ACCOUNT_CREATED_ROUTING_KEY = "account.created";

    @Value("${messaging.rabbit.exchange:account.events}")
    private String exchangeName;

    @Value("${messaging.rabbit.queues.account-created:account.created}")
    private String accountCreatedQueueName;

    @Bean
    public DirectExchange accountExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue accountCreatedQueue() {
        return new Queue(accountCreatedQueueName, true);
    }

    @Bean
    public Binding accountCreatedBinding() {
        return BindingBuilder
                .bind(accountCreatedQueue())
                .to(accountExchange())
                .with(ACCOUNT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
