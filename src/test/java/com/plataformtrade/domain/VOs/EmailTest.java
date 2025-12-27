package com.plataformtrade.domain.VOs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email Value Object Tests")
class EmailTest {

    @ParameterizedTest
    @DisplayName("Should create valid email")
    @ValueSource(strings = {
            "user@example.com",
            "john.doe@company.com",
            "test+tag@gmail.com",
            "user_123@domain.co.uk",
            "UPPERCASE@DOMAIN.COM"
    })
    void shouldCreateValidEmail(String validEmail) {
        // Act
        Email email = new Email(validEmail);

        // Assert
        assertNotNull(email);
        assertEquals(validEmail.toLowerCase(), email.getValue());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for invalid email")
    @ValueSource(strings = {
            "invalid",
            "@example.com",
            "user@",
            "user @example.com",
            "user@.com",
            "user..name@example.com",
            "",
            "   "
    })
    void shouldThrowExceptionForInvalidEmail(String invalidEmail) {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Email(invalidEmail)
        );
    }

    @Test
    @DisplayName("Should throw exception for null email")
    void shouldThrowExceptionForNullEmail() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Email(null)
        );
    }

    @Test
    @DisplayName("Should normalize email to lowercase")
    void shouldNormalizeEmailToLowercase() {
        // Arrange
        String upperCaseEmail = "USER@EXAMPLE.COM";

        // Act
        Email email = new Email(upperCaseEmail);

        // Assert
        assertEquals("user@example.com", email.getValue());
    }

    @Test
    @DisplayName("Should trim whitespace")
    void shouldTrimWhitespace() {
        // Arrange
        String emailWithSpaces = "  user@example.com  ";

        // Act
        Email email = new Email(emailWithSpaces);

        // Assert
        assertEquals("user@example.com", email.getValue());
    }

    @Test
    @DisplayName("Should extract domain")
    void shouldExtractDomain() {
        // Arrange
        Email email = new Email("user@example.com");

        // Act & Assert
        assertEquals("example.com", email.getDomain());
    }

    @Test
    @DisplayName("Should extract local part")
    void shouldExtractLocalPart() {
        // Arrange
        Email email = new Email("user@example.com");

        // Act & Assert
        assertEquals("user", email.getLocalPart());
    }

    @Test
    @DisplayName("Should be equal when emails are the same")
    void shouldBeEqualWhenEmailsAreTheSame() {
        // Arrange
        Email email1 = new Email("user@example.com");
        Email email2 = new Email("USER@EXAMPLE.COM");

        // Act & Assert
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when emails are different")
    void shouldNotBeEqualWhenEmailsAreDifferent() {
        // Arrange
        Email email1 = new Email("user1@example.com");
        Email email2 = new Email("user2@example.com");

        // Act & Assert
        assertNotEquals(email1, email2);
    }
}