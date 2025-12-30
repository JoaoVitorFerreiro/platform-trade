package com.plataformtrade.application.usecases;

import com.plataformtrade.application.dtos.AccountResponse;
import com.plataformtrade.application.dtos.CreateAccountRequest;
import com.plataformtrade.application.events.DomainEventPublisher;
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
    private final DomainEventPublisher eventPublisher;

    public Signup(
            AccountRepository accountRepository,
            PasswordHasher passwordHasher,
            DomainEventPublisher eventPublisher
    ) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository must not be null");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "passwordHasher must not be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher must not be null");
    }

    @Transactional
    public AccountResponse execute(CreateAccountRequest request) {
        Account account = Account.create(
                request.name(),
                request.document(),
                request.password(),
                request.email(),
                passwordHasher
        );

        Account savedAccount = accountRepository.save(account);

        eventPublisher.publishAll(account.pullDomainEvents());

        return new AccountResponse(
                savedAccount.getAccountId(),
                savedAccount.getName().getValue(),
                savedAccount.getEmail().getValue(),
                savedAccount.getDocument().getValue()
        );
    }
}
