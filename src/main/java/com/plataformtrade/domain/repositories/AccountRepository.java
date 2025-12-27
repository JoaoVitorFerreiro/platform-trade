package com.plataformtrade.domain.repositories;

import com.plataformtrade.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    List<Account> findAll();
    Optional<Account> findById(String accountId);
    void deleteById(String accountId);
}
