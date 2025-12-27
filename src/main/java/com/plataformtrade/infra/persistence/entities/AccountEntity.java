package com.plataformtrade.infra.persistence.entities;

import com.plataformtrade.domain.VOs.Name;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @Column(name = "account_id", length = 36)
    private String accountId;

    private String name;

    protected AccountEntity(){}

    public AccountEntity(String accountId, String name){
        this.accountId = accountId;
        this.name = name;
    }

    public AccountEntity(String accountId, Name name) {
        this.accountId = accountId;
        this.name = name.getValue();
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
}
