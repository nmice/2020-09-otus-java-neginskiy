package ru.otus.jpql.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jpql.dao.AccountDao;
import ru.otus.jpql.exception.DbServiceException;
import ru.otus.jpql.model.Account;
import ru.otus.jpql.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceAccountImpl implements DBServiceAccount {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceAccountImpl.class);

    private final AccountDao accountDao;

    public DbServiceAccountImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public String saveAccount(Account account) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                String accountId = accountDao.insertOrUpdate(account);
                sessionManager.commitSession();

                logger.info("created account: {}", accountId);
                return accountId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccountById(String id) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Account> accountOptional = accountDao.findById(id);

                logger.info("account: {}", accountOptional.orElse(null));
                return accountOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}