package com.plataformtrade.application.usecases;

import com.plataformtrade.application.dtos.AccountResponse;
import com.plataformtrade.application.dtos.CreateAccountRequest;
import com.plataformtrade.domain.Account;
import com.plataformtrade.domain.repositories.AccountRepository;
import com.plataformtrade.domain.repositories.PasswordHasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class Signup {
    private final AccountRepository accountRepository;
    private final PasswordHasher passwordHasher;

    public Signup(AccountRepository accountRepository, PasswordHasher passwordHasher) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository must not be null");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "passwordHasher must not be null");
    }

    @Transactional
    public AccountResponse execute(CreateAccountRequest request) {
        // 1. Create Account (domain logic)
        Account account = Account.create(
                request.name(),
                request.document(),
                request.password(),
                request.email(),
                passwordHasher
        );

        // 2. Persist Account (infrastructure)
        Account savedAccount = accountRepository.save(account);

        // 3. Return Response DTO
        return new AccountResponse(
                savedAccount.getAccountId(),
                savedAccount.getName().getValue(),
                savedAccount.getEmail().getValue(),
                savedAccount.getDocument().getValue()
        );
    }
}
