package com.plataformtrade.infra.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfig {
    @Value("${resilience.rabbit.circuitbreaker.failure-rate-threshold:50}")
    private float failureRateThreshold;

    @Value("${resilience.rabbit.circuitbreaker.sliding-window-size:10}")
    private int slidingWindowSize;

    @Value("${resilience.rabbit.circuitbreaker.wait-duration-open-state-seconds:10}")
    private int waitDurationOpenStateSeconds;

    @Value("${resilience.rabbit.circuitbreaker.permitted-calls-in-half-open-state:3}")
    private int permittedCallsInHalfOpen;

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        io.github.resilience4j.circuitbreaker.CircuitBreakerConfig config =
                io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                        .failureRateThreshold(failureRateThreshold)
                        .slidingWindowSize(slidingWindowSize)
                        .waitDurationInOpenState(Duration.ofSeconds(waitDurationOpenStateSeconds))
                        .permittedNumberOfCallsInHalfOpenState(permittedCallsInHalfOpen)
                        .build();
        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public CircuitBreaker rabbitPublisherCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("rabbitPublisher");
    }
}
