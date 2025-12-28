package com.plataformtrade.domain;

import com.plataformtrade.domain.VOs.Document;
import com.plataformtrade.domain.VOs.Email;
import com.plataformtrade.domain.VOs.Name;
import com.plataformtrade.domain.VOs.Password;

import java.util.UUID;

public class Account {
    private final String accountId;
    private Name name;
    private Document document;
    private Password password;
    private Email email;


    public Account(String accountId, String name, String document, String password, String email) {
        this.accountId = accountId;
        this.name = new Name(name);
        this.document = new Document(document);
        this.password = new Password(password);
        this.email = new Email(email);
    }

    public static Account create(String name, String document, String password, String email){
        String accountId = UUID.randomUUID().toString();
        return new Account(accountId, name, document, password, email);
    }

    public static Account restore(String accountId, String name, String document, String password, String email) {
        return new Account(accountId, name, document, password, email);
    }

    public String getAccountId(){
        return this.accountId;
    }

    public Name getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = new Name(name);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }
}
