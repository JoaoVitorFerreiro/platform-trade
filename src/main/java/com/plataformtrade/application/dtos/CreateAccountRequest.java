package com.plataformtrade.application.dtos;

public record CreateAccountRequest(String name, String email, String password, String document) {
}
