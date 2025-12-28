package com.plataformtrade.application.services;

import com.plataformtrade.application.dtos.AccountResponse;
import com.plataformtrade.application.dtos.CreateAccountRequest;
import com.plataformtrade.domain.Account;
import com.plataformtrade.domain.exceptions.NotFoundException;
import com.plataformtrade.domain.repositories.AccountRepository;
import com.plataformtrade.domain.repositories.PasswordHasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordHasher passwordHasher;

    public AccountService(AccountRepository accountRepository, PasswordHasher passwordHasher) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository must not be null");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "passwordHasher must not be null");
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request){
        Account account = Account.create(
                request.name(),
                request.document(),
                request.password(),
                request.email(),
                passwordHasher
        );
        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getAccountId(),
                savedAccount.getName().getValue(),
                savedAccount.getEmail().getValue(),
                savedAccount.getDocument().getValue()
        );
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + accountId));

        return new AccountResponse(
                account.getAccountId(),
                account.getName().getValue(),
                account.getEmail().getValue(),
                account.getDocument().getValue()
        );
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountResponse(
                        account.getAccountId(),
                        account.getName().getValue(),
                        account.getEmail().getValue(),
                        account.getDocument().getValue()
                ))
                .collect(Collectors.toList());
    }

}
