package com.plataformtrade.application.usecases;

import com.plataformtrade.application.dtos.AccountResponse;
import com.plataformtrade.domain.Account;
import com.plataformtrade.domain.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GetAllAccounts {
    private final AccountRepository accountRepository;

    public GetAllAccounts(AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository must not be null");
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> execute() {
        return accountRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getAccountId(),
                account.getName().getValue(),
                account.getEmail().getValue(),
                account.getDocument().getValue()
        );
    }
}
