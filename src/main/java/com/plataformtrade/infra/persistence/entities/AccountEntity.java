package com.plataformtrade.infra.persistence.entities;

import com.plataformtrade.domain.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @Column(name = "account_id", length = 36, nullable = false)
    private String accountId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "document", nullable = false, unique = true, length = 14)
    private String document;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    protected AccountEntity() {}

    public AccountEntity(String accountId, String name, String document, String password, String email) {
        this.accountId = accountId;
        this.name = name;
        this.document = document;
        this.password = password;
        this.email = email;
    }

    public AccountEntity(Account account) {
        this.accountId = account.getAccountId();
        this.name = account.getName().getValue();
        this.document = account.getDocument().getValue();
        this.password = account.getEmail().getValue();
        this.email = account.getEmail().getValue();
    }

    public Account toDomain() {
        return Account.restore(
                this.accountId,
                this.name,
                this.document,
                this.password,
                this.email
        );
    }

    public static AccountEntity fromDomain(Account account) {
        return new AccountEntity(account);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}