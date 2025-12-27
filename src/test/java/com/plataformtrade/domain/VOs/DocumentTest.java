package com.plataformtrade.domain.VOs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Document Value Object Tests")
class DocumentTest {

    @Test
    @DisplayName("Should create valid document with formatted CPF")
    void shouldCreateValidDocumentWithFormattedCPF() {
        String cpf = "123.456.789-09";

        Document document = new Document(cpf);

        assertNotNull(document);
        assertEquals("12345678909", document.getValue());
    }

    @Test
    @DisplayName("Should create valid document with unformatted CPF")
    void shouldCreateValidDocumentWithUnformattedCPF() {
        String cpf = "12345678909";

        Document document = new Document(cpf);

        assertNotNull(document);
        assertEquals("12345678909", document.getValue());
    }

    @Test
    @DisplayName("Should throw exception for invalid CPF")
    void shouldThrowExceptionForInvalidCPF() {
        String invalidCpf = "123.456.789-00";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Document(invalidCpf)
        );

        assertEquals("Invalid document", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for CPF with all same digits")
    void shouldThrowExceptionForCPFWithAllSameDigits() {
        String invalidCpf = "111.111.111-11";

        assertThrows(
                IllegalArgumentException.class,
                () -> new Document(invalidCpf)
        );
    }

    @Test
    @DisplayName("Should throw exception for null CPF")
    void shouldThrowExceptionForNullCPF() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Document(null)
        );
    }

    @Test
    @DisplayName("Should throw exception for empty CPF")
    void shouldThrowExceptionForEmptyCPF() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Document("")
        );
    }

    @Test
    @DisplayName("Should throw exception for CPF with invalid length")
    void shouldThrowExceptionForCPFWithInvalidLength() {
        String invalidCpf = "123.456";

        assertThrows(
                IllegalArgumentException.class,
                () -> new Document(invalidCpf)
        );
    }

    @Test
    @DisplayName("Should be equal when CPFs are the same")
    void shouldBeEqualWhenCPFsAreTheSame() {
        Document doc1 = new Document("123.456.789-09");
        Document doc2 = new Document("12345678909");

        assertEquals(doc1, doc2);
        assertEquals(doc1.hashCode(), doc2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when CPFs are different")
    void shouldNotBeEqualWhenCPFsAreDifferent() {
        Document doc1 = new Document("123.456.789-09");
        Document doc2 = new Document("987.654.321-00");

        assertNotEquals(doc1, doc2);
    }
}