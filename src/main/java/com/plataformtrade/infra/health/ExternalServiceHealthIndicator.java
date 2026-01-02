package com.plataformtrade.infra.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
@ConditionalOnProperty(prefix = "external.health", name = "enabled", havingValue = "true")
public class ExternalServiceHealthIndicator implements HealthIndicator {
    private final WebClient webClient;
    private final String url;
    private final Duration timeout;

    public ExternalServiceHealthIndicator(
            WebClient.Builder webClientBuilder,
            @Value("${external.health.url:}") String url,
            @Value("${external.health.timeout-ms:2000}") long timeoutMs
    ) {
        this.webClient = webClientBuilder.build();
        this.url = url;
        this.timeout = Duration.ofMillis(timeoutMs);
    }

    @Override
    public Health health() {
        if (url == null || url.isBlank()) {
            return Health.unknown().withDetail("external", "url not configured").build();
        }

        try {
            Integer status = webClient.get()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity()
                    .map(response -> response.getStatusCode().value())
                    .block(timeout);

            if (status != null && status >= 200 && status < 300) {
                return Health.up().withDetail("external", "reachable").build();
            }
            return Health.down().withDetail("external", "status " + status).build();
        } catch (Exception ex) {
            return Health.down(ex).build();
        }
    }
}
