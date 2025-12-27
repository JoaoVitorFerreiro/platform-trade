package com.plataformtrade.domain.VOs;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
public final class Email {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    private final String value;

    public Email(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.value = value.toLowerCase().trim();
    }

    private boolean isValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String trimmed = email.trim();

        if (trimmed.length() > 254) {
            return false;
        }

        if (!trimmed.contains("@")) {
            return false;
        }

        if (trimmed.startsWith(".") || trimmed.endsWith(".")) {
            return false;
        }

        if (trimmed.contains("..")) {
            return false;
        }

        String[] parts = trimmed.split("@");
        if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            return false;
        }

        if (parts[1].startsWith(".")) {
            return false;
        }

        return trimmed.matches(EMAIL_REGEX);
    }

    public String getValue() {
        return value;
    }

    public String getDomain() {
        return value.substring(value.indexOf("@") + 1);
    }

    public String getLocalPart() {
        return value.substring(0, value.indexOf("@"));
    }

    public boolean isDomain(String domain) {
        return this.getDomain().equalsIgnoreCase(domain);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
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