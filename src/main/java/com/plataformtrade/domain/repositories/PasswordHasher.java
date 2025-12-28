package com.plataformtrade.domain.repositories;

public interface PasswordHasher {
    /**
     * Hashes a plaintext password with a strong one-way algorithm and salt.
     */
    String hash(String password);

    /**
     * Compares a plaintext password against a previously hashed value.
     */
    boolean matches(String password, String hash);
}
