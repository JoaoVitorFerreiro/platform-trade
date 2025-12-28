package com.plataformtrade.domain.VOs;

import java.util.Objects;
import java.util.regex.Pattern;

@SuppressWarnings("ClassCanBeRecord")
public final class Password {
    private static final int MIN_LENGTH = 8;

    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*[0-9].*");

    private final String value;

    private final transient boolean hasLowerCase;
    private final transient boolean hasUpperCase;
    private final transient boolean hasDigit;

    public Password(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid password");
        }
        this.value = value;

        this.hasLowerCase = LOWERCASE_PATTERN.matcher(value).matches();
        this.hasUpperCase = UPPERCASE_PATTERN.matcher(value).matches();
        this.hasDigit = DIGIT_PATTERN.matcher(value).matches();
    }

    private Password(String value, boolean skipValidation) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!skipValidation && !isValid(value)) {
            throw new IllegalArgumentException("Invalid password");
        }
        this.value = value;

        this.hasLowerCase = LOWERCASE_PATTERN.matcher(value).matches();
        this.hasUpperCase = UPPERCASE_PATTERN.matcher(value).matches();
        this.hasDigit = DIGIT_PATTERN.matcher(value).matches();
    }

    public static Password fromHashed(String hashed) {
        return new Password(hashed, true);
    }

    private boolean isValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        if (password.length() < MIN_LENGTH) {
            return false;
        }

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;

            if (hasLower && hasUpper && hasDigit) {
                return true;
            }
        }

        return hasLower && hasUpper && hasDigit;
    }

    public String getValue() {
        return value;
    }

    public boolean hasMinimumLength() {
        return value.length() >= MIN_LENGTH;
    }

    public boolean hasLowerCase() {
        return hasLowerCase;
    }

    public boolean hasUpperCase() {
        return hasUpperCase;
    }

    public boolean hasDigit() {
        return hasDigit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "********";
    }
}
