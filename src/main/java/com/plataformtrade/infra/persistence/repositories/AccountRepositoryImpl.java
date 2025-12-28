package com.plataformtrade.infra.persistence.repositories;

import com.plataformtrade.domain.Account;
import com.plataformtrade.domain.repositories.AccountRepository;
import com.plataformtrade.infra.persistence.entities.AccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface AccountJpaRepository extends JpaRepository<AccountEntity, String> {
}

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);
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
        List<AccountEntity> entities = jpaRepository.findAll();
        List<Account> accounts = new ArrayList<>(entities.size());

        for (AccountEntity entity : entities) {
            try {
                accounts.add(Account.restore(
                        entity.getAccountId(),
                        entity.getName(),
                        entity.getDocument(),
                        entity.getPassword(),
                        entity.getEmail()
                ));
            } catch (RuntimeException ex) {
                logger.error(
                        "Failed to map AccountEntity: accountId={}, name={}, document={}, email={}",
                        entity.getAccountId(),
                        entity.getName(),
                        entity.getDocument(),
                        entity.getEmail(),
                        ex
                );
                throw ex;
            }
        }

        return accounts;
    }

    @Override
    public void deleteById(String accountId) {
        jpaRepository.deleteById(accountId);
    }
}
