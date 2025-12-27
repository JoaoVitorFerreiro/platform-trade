package com.plataformtrade.application.services;

import com.plataformtrade.application.dtos.AccountResponse;
import com.plataformtrade.application.dtos.CreateAccountRequest;
import com.plataformtrade.domain.Account;
import com.plataformtrade.domain.exceptions.NotFoundException;
import com.plataformtrade.domain.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request){
        Account account = Account.create(request.name());
        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getAccountId(),
                savedAccount.getName().getValue()
        );
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + accountId));

        return new AccountResponse(
                account.getAccountId(),
                account.getName().getValue()
        );
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountResponse(
                        account.getAccountId(),
                        account.getName().getValue()
                ))
                .collect(Collectors.toList());
    }

}
