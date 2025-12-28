package com.plataformtrade.infra.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountControllerDebugTest {
    private static final String BASE_URL = "/api/v1/accounts";

    @Autowired
    private TestRestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("DEBUG: Test exact user request")
    void testExactUserRequest() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("name", "John Doe");
        request.put("email", "john.doe@email.com");
        request.put("password", "Password123");
        request.put("document", "323.019.130-74");

        System.out.println("=== DEBUG: Sending request ===");
        System.out.println("Request: " + objectMapper.writeValueAsString(request));

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        System.out.println("=== DEBUG: Response ===");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode(),
            "Expected CREATED but got: " + response.getBody());
    }
}
