package com.plataformtrade.domain;

import com.plataformtrade.domain.VOs.Name;

import java.util.UUID;

public class Account {
    private Name name;
    private final String accountId;


    public Account(String accountId, String name) {
        this.accountId = accountId;
        this.name = new Name(name);
    }

    public static Account create(String name){
        String accountId = UUID.randomUUID().toString();
        return new Account(accountId, name);
    }

    public static Account restore(String accountId, String name) {
        return new Account(accountId, name);
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
}
