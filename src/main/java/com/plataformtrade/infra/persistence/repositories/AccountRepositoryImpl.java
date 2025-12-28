package com.plataformtrade.infra.persistence.repositories;

import com.plataformtrade.domain.Account;
import com.plataformtrade.domain.repositories.AccountRepository;
import com.plataformtrade.infra.persistence.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface AccountJpaRepository extends JpaRepository<AccountEntity, String> {
}

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private final AccountJpaRepository jpaRepository;

    public AccountRepositoryImpl(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity = new AccountEntity(
                account.getAccountId(),
                account.getName().getValue(),
                account.getDocument().getValue(),
                account.getPassword().getValue(),
                account.getEmail().getValue()
        );
        jpaRepository.save(entity);
        return account;
    }

    @Override
    public Optional<Account> findById(String accountId) {
        return jpaRepository.findById(accountId)
                .map(entity -> Account.restore(
                        entity.getAccountId(),
                        entity.getName(),
                        entity.getDocument(),
                        entity.getPassword(),
                        entity.getEmail()
                ));
    }

    @Override
    public List<Account> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(entity -> Account.restore(
                        entity.getAccountId(),
                        entity.getName(),
                        entity.getDocument(),
                        entity.getPassword(),
                        entity.getEmail()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String accountId) {
        jpaRepository.deleteById(accountId);
    }
}