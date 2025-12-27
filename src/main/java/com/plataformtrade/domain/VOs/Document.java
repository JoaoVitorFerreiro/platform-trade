package com.plataformtrade.domain.VOs;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
public final class Document {
    private static final int VALID_LENGTH = 11;
    private final String value;

    public Document(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid document");
        }
        this.value = clean(value);
    }

    private boolean isValid(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }

        cpf = clean(cpf);

        if (cpf.length() != VALID_LENGTH) {
            return false;
        }

        if (allDigitsTheSame(cpf)) {
            return false;
        }

        int dg1 = calculateDigit(cpf, 10);
        int dg2 = calculateDigit(cpf, 11);

        return extractCheckDigit(cpf).equals(String.valueOf(dg1) + dg2);
    }

    private String clean(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private boolean allDigitsTheSame(String cpf) {
        char firstDigit = cpf.charAt(0);
        return cpf.chars().allMatch(c -> c == firstDigit);
    }

    private int calculateDigit(String cpf, int factor) {
        int total = 0;

        for (char digit : cpf.toCharArray()) {
            if (factor > 1) {
                total += Character.getNumericValue(digit) * factor--;
            }
        }

        int rest = total % 11;
        return (rest < 2) ? 0 : 11 - rest;
    }

    private String extractCheckDigit(String cpf) {
        return cpf.substring(9);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(value, document.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}