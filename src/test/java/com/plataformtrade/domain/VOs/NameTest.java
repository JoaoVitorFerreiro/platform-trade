package com.plataformtrade.domain.VOs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Name Value Object Tests")
class NameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "João Silva",
            "Maria Santos",
            "José",
            "Ana",
            "José da Silva Santos",
            "Pedro Álvares Cabral"
    })
    @DisplayName("Should create valid names")
    void shouldCreateValidNames(String validName) {
        Name name = new Name(validName);

        assertNotNull(name);
        assertEquals(validName.trim(), name.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "A",
            "João123",
            "Maria@Silva",
            "Pedro#Santos"
    })
    @DisplayName("Should throw exception for invalid names")
    void shouldThrowExceptionForInvalidNames(String invalidName) {
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Name(null));
    }

    @Test
    @DisplayName("Should trim whitespace")
    void shouldTrimWhitespace() {
        Name name = new Name("  João Silva  ");
        assertEquals("João Silva", name.getValue());
    }

    @Test
    @DisplayName("Should be equal when names are the same")
    void shouldBeEqualWhenNamesAreTheSame() {
        Name name1 = new Name("João Silva");
        Name name2 = new Name("João Silva");

        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when names are different")
    void shouldNotBeEqualWhenNamesAreDifferent() {
        Name name1 = new Name("João Silva");
        Name name2 = new Name("Maria Santos");

        assertNotEquals(name1, name2);
    }

    @Test
    @DisplayName("Should accept names with accents")
    void shouldAcceptNamesWithAccents() {
        Name name1 = new Name("João");
        Name name2 = new Name("María");

        assertEquals("João", name1.getValue());
        assertEquals("María", name2.getValue());
    }
}