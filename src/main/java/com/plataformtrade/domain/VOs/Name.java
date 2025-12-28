package com.plataformtrade.domain.VOs;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
public final class Name {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;

    private final String value;

    public Name(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid name");
        }
        this.value = value.trim();
    }

    private boolean isValid(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        String trimmed = name.trim();

        if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
            return false;
        }

        return trimmed.matches("^[a-zA-ZÀ-ÿ]+([ '-][a-zA-ZÀ-ÿ]+)*$");
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(value, name.value);
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