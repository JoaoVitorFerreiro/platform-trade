package com.plataformtrade.application.usecases;

import com.plataformtrade.application.dtos.AccountResponse;
import com.plataformtrade.domain.Account;
import com.plataformtrade.domain.exceptions.NotFoundException;
import com.plataformtrade.domain.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class GetAccount {
    private final AccountRepository accountRepository;

    public GetAccount(AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository must not be null");
    }

    @Transactional(readOnly = true)
    public AccountResponse execute(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + accountId));

        return new AccountResponse(
                account.getAccountId(),
                account.getName().getValue(),
                account.getEmail().getValue(),
                account.getDocument().getValue()
        );
    }
}
