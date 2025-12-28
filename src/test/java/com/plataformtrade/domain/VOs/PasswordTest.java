package com.plataformtrade.domain.VOs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Password Value Object Tests")
class PasswordTest {

    @Test
    @DisplayName("Should create a valid password")
    void shouldCreateValidPassword() {
        String validPassword = "Teste123";
        Password password = new Password(validPassword);

        assertNotNull(password);
        assertEquals(validPassword, password.getValue());
    }

    @Test
    @DisplayName("Should throw exception when password is null")
    void shouldThrowExceptionWhenPasswordIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(null)
        );

        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when password is empty")
    void shouldThrowExceptionWhenPasswordIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password("")
        );

        assertEquals("Invalid password", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test12", "Abc123", "Pass1", "aA1"})
    @DisplayName("Should throw exception when password has less than 8 characters")
    void shouldThrowExceptionWhenPasswordHasLessThan8Characters(String invalidPassword) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(invalidPassword)
        );

        assertEquals("Invalid password", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"TESTE123", "PASSWORD1", "ABCDEFGH1"})
    @DisplayName("Should throw exception when password has no lowercase letter")
    void shouldThrowExceptionWhenPasswordHasNoLowercaseLetter(String invalidPassword) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(invalidPassword)
        );

        assertEquals("Invalid password", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"teste123", "password1", "abcdefgh1"})
    @DisplayName("Should throw exception when password has no uppercase letter")
    void shouldThrowExceptionWhenPasswordHasNoUppercaseLetter(String invalidPassword) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(invalidPassword)
        );

        assertEquals("Invalid password", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"TesteSenha", "Password", "AbcdEfgh"})
    @DisplayName("Should throw exception when password has no digit")
    void shouldThrowExceptionWhenPasswordHasNoDigit(String invalidPassword) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(invalidPassword)
        );

        assertEquals("Invalid password", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Teste123",
            "Password1",
            "SenhaForte123",
            "MyP@ssw0rd",
            "Abcd1234"
    })
    @DisplayName("Should create password with valid formats")
    void shouldCreatePasswordWithValidFormats(String validPassword) {
        Password password = new Password(validPassword);

        assertNotNull(password);
        assertEquals(validPassword, password.getValue());
    }

    @Test
    @DisplayName("Should validate minimum length correctly")
    void shouldValidateMinimumLengthCorrectly() {
        Password password = new Password("Teste123");

        assertTrue(password.hasMinimumLength());
    }

    @Test
    @DisplayName("Should validate lowercase presence correctly")
    void shouldValidateLowercasePresenceCorrectly() {
        Password password = new Password("Teste123");

        assertTrue(password.hasLowerCase());
    }

    @Test
    @DisplayName("Should validate uppercase presence correctly")
    void shouldValidateUppercasePresenceCorrectly() {
        Password password = new Password("Teste123");

        assertTrue(password.hasUpperCase());
    }

    @Test
    @DisplayName("Should validate digit presence correctly")
    void shouldValidateDigitPresenceCorrectly() {
        Password password = new Password("Teste123");

        assertTrue(password.hasDigit());
    }

    @Test
    @DisplayName("Should return masked string in toString")
    void shouldReturnMaskedStringInToString() {
        Password password = new Password("Teste123");

        assertEquals("********", password.toString());
    }

    @Test
    @DisplayName("Should be equal when passwords have same value")
    void shouldBeEqualWhenPasswordsHaveSameValue() {
        Password password1 = new Password("Teste123");
        Password password2 = new Password("Teste123");

        assertEquals(password1, password2);
        assertEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when passwords have different values")
    void shouldNotBeEqualWhenPasswordsHaveDifferentValues() {
        Password password1 = new Password("Teste123");
        Password password2 = new Password("Teste456");

        assertNotEquals(password1, password2);
        assertNotEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        Password password = new Password("Teste123");

        assertEquals(password, password);
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        Password password = new Password("Teste123");

        assertNotEquals(null, password);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void shouldNotBeEqualToDifferentClass() {
        Password password = new Password("Teste123");
        String string = "Teste123";

        assertNotEquals(password, string);
    }

    @Test
    @DisplayName("Should accept password with special characters")
    void shouldAcceptPasswordWithSpecialCharacters() {
        Password password = new Password("Test@123!");

        assertNotNull(password);
        assertEquals("Test@123!", password.getValue());
    }

    @Test
    @DisplayName("Should accept password with exactly 8 characters")
    void shouldAcceptPasswordWithExactly8Characters() {
        Password password = new Password("Teste123");

        assertNotNull(password);
        assertEquals(8, password.getValue().length());
    }

    @Test
    @DisplayName("Should accept very long password")
    void shouldAcceptVeryLongPassword() {
        String longPassword = "TesteSenhaForte123456789ABCDEFGH";
        Password password = new Password(longPassword);

        assertNotNull(password);
        assertEquals(longPassword, password.getValue());
    }
}