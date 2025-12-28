package com.plataformtrade.infra.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountControllerE2ETest {
    private static final String BASE_URL = "/api/v1/accounts";
    private static final String VALID_CPF_1 = "12345678909";
    private static final String VALID_CPF_2 = "11144477735";

    @Autowired
    private TestRestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("POST /accounts should create a new account")
    void shouldCreateAccount() throws Exception {
        Map<String, Object> request = createRequest(
                "Joao Silva",
                "joao@email.com",
                "Senha123",
                VALID_CPF_1
        );

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JsonNode body = objectMapper.readTree(response.getBody());
        assertTrue(body.get("success").asBoolean());
        assertEquals("Resource created successfully", body.get("message").asText());
        assertNotNull(body.get("data").get("accountId").asText());
        assertEquals("Joao Silva", body.get("data").get("name").asText());
        assertEquals("joao@email.com", body.get("data").get("email").asText());
        assertEquals(VALID_CPF_1, body.get("data").get("document").asText());
    }

    @Test
    @DisplayName("GET /accounts/{id} should return the account")
    void shouldGetAccountById() throws Exception {
        String accountId = createAccountAndReturnId(
                "Maria Santos",
                "maria@email.com",
                "Password1",
                VALID_CPF_2
        );

        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/" + accountId, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JsonNode body = objectMapper.readTree(response.getBody());
        assertTrue(body.get("success").asBoolean());
        assertEquals(accountId, body.get("data").get("accountId").asText());
        assertEquals("Maria Santos", body.get("data").get("name").asText());
        assertEquals("maria@email.com", body.get("data").get("email").asText());
        assertEquals(VALID_CPF_2, body.get("data").get("document").asText());
    }

    @Test
    @DisplayName("GET /accounts should return a page response")
    void shouldListAccounts() throws Exception {
        createAccountAndReturnId(
                "Joao Silva",
                "joao@email.com",
                "Senha123",
                VALID_CPF_1
        );
        createAccountAndReturnId(
                "Maria Santos",
                "maria@email.com",
                "Password1",
                VALID_CPF_2
        );

        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JsonNode body = objectMapper.readTree(response.getBody());
        assertTrue(body.get("success").asBoolean());
        assertEquals(2, body.get("data").get("totalElements").asInt());
        assertEquals(2, body.get("data").get("content").size());
    }

    @Test
    @DisplayName("POST /accounts should validate name")
    void shouldValidateName() throws Exception {
        Map<String, Object> request = createRequest(
                "J",
                "joao@email.com",
                "Senha123",
                VALID_CPF_1
        );

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonNode body = objectMapper.readTree(response.getBody());
        assertEquals(400, body.get("status").asInt());
        assertEquals("Bad Request", body.get("error").asText());
        assertEquals("Invalid name", body.get("message").asText());
    }

    @Test
    @DisplayName("POST /accounts should validate document")
    void shouldValidateDocument() throws Exception {
        Map<String, Object> request = createRequest(
                "Joao Silva",
                "joao@email.com",
                "Senha123",
                "99999999999"
        );

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonNode body = objectMapper.readTree(response.getBody());
        assertEquals("Invalid document", body.get("message").asText());
    }

    @Test
    @DisplayName("POST /accounts should validate email")
    void shouldValidateEmail() throws Exception {
        Map<String, Object> request = createRequest(
                "Joao Silva",
                "email-invalido",
                "Senha123",
                VALID_CPF_1
        );

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonNode body = objectMapper.readTree(response.getBody());
        assertEquals("Invalid email", body.get("message").asText());
    }

    @Test
    @DisplayName("POST /accounts should validate password")
    void shouldValidatePassword() throws Exception {
        Map<String, Object> request = createRequest(
                "Joao Silva",
                "joao@email.com",
                "123",
                VALID_CPF_1
        );

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonNode body = objectMapper.readTree(response.getBody());
        assertEquals("Invalid password", body.get("message").asText());
    }

    @Test
    @DisplayName("GET /accounts/{id} should return 404 when not found")
    void shouldReturnNotFoundWhenAccountMissing() throws Exception {
        String missingId = "123e4567-e89b-12d3-a456-426614174000";

        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/" + missingId, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        JsonNode body = objectMapper.readTree(response.getBody());
        assertEquals(404, body.get("status").asInt());
        assertEquals("Not Found", body.get("error").asText());
        assertEquals("Account not found with id: " + missingId, body.get("message").asText());
    }

    private String createAccountAndReturnId(
            String name,
            String email,
            String password,
            String document
    ) throws Exception {
        Map<String, Object> request = createRequest(name, email, password, document);
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JsonNode body = objectMapper.readTree(response.getBody());
        return body.get("data").get("accountId").asText();
    }

    private Map<String, Object> createRequest(String name, String email, String password, String document) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("email", email);
        request.put("password", password);
        request.put("document", document);
        return request;
    }
}
