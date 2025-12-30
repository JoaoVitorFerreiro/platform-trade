package com.plataformtrade.infra.resilience;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RetryConfig {
    @Value("${resilience.rabbit.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${resilience.rabbit.retry.wait-duration-ms:500}")
    private int waitDurationMs;

    @Bean
    public RetryRegistry retryRegistry() {
        io.github.resilience4j.retry.RetryConfig config =
                io.github.resilience4j.retry.RetryConfig.custom()
                        .maxAttempts(maxAttempts)
                        .waitDuration(Duration.ofMillis(waitDurationMs))
                        .retryExceptions(Exception.class)
                        .build();
        return RetryRegistry.of(config);
    }

    @Bean
    public Retry rabbitPublisherRetry(RetryRegistry registry) {
        return registry.retry("rabbitPublisher");
    }
}
