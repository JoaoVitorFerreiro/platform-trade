package com.plataformtrade.domain.VOs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Name Value Object Tests")
class NameTest {

    @Test
    @DisplayName("Should create valid name with single word")
    void shouldCreateValidNameWithSingleWord() {
        // Arrange
        String validName = "John";

        // Act
        Name name = new Name(validName);

        // Assert
        assertNotNull(name);
        assertEquals("John", name.getValue());
    }

    @ParameterizedTest
    @DisplayName("Should create valid name with various formats")
    @ValueSource(strings = {"John", "MARY", "alice", "JoHn", "A"})
    void shouldCreateValidNameWithVariousFormats(String validName) {
        // Act
        Name name = new Name(validName);

        // Assert
        assertNotNull(name);
        assertEquals(validName, name.getValue());
    }

    @Test
    @DisplayName("Should throw exception for null name")
    void shouldThrowExceptionForNullName() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Name(null)
        );

        assertEquals("Invalid name", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for empty or blank name")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void shouldThrowExceptionForEmptyOrBlankName(String invalidName) {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Name(invalidName)
        );
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for name with numbers")
    @ValueSource(strings = {"John123", "123", "John1", "1John"})
    void shouldThrowExceptionForNameWithNumbers(String invalidName) {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Name(invalidName)
        );
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for name with special characters")
    @ValueSource(strings = {"John@", "John!", "Jo#hn", "John$", "John%", "Jo&hn"})
    void shouldThrowExceptionForNameWithSpecialCharacters(String invalidName) {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Name(invalidName)
        );
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for name with spaces")
    @ValueSource(strings = {"John Doe", "Mary Jane", " John", "John ", "Jo hn"})
    void shouldThrowExceptionForNameWithSpaces(String invalidName) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Name(invalidName)
        );

        assertEquals("Invalid name", exception.getMessage());
    }

    @Test
    @DisplayName("Should be equal when names are the same")
    void shouldBeEqualWhenNamesAreTheSame() {
        // Arrange
        Name name1 = new Name("John");
        Name name2 = new Name("John");

        // Act & Assert
        assertEquals(name1.getValue(), name2.getValue());
    }

    @Test
    @DisplayName("Should not be equal when names are different")
    void shouldNotBeEqualWhenNamesAreDifferent() {
        // Arrange
        Name name1 = new Name("John");
        Name name2 = new Name("Mary");

        // Act & Assert
        assertNotEquals(name1.getValue(), name2.getValue());
    }

    @Test
    @DisplayName("Should be case sensitive")
    void shouldBeCaseSensitive() {
        // Arrange
        Name name1 = new Name("John");
        Name name2 = new Name("john");

        // Act & Assert
        assertNotEquals(name1.getValue(), name2.getValue());
    }

    @Test
    @DisplayName("Should throw exception for name with accents")
    void shouldThrowExceptionForNameWithAccents() {
        // Arrange & Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Name("João")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Name("María")
        );
    }

    @Test
    @DisplayName("Should preserve original value")
    void shouldPreserveOriginalValue() {
        // Arrange
        String originalValue = "Alice";

        // Act
        Name name = new Name(originalValue);

        // Assert
        assertEquals(originalValue, name.getValue());
        assertSame(originalValue, name.getValue());
    }
}