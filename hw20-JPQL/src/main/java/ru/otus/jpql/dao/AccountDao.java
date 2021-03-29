package ru.otus.jpql.dao;

import ru.otus.jpql.model.Account;
import ru.otus.jpql.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {
    Optional<Account> findById(String id);
    //List<Account> findAll();

    String insert(Account account);

    void update(Account account);

    String insertOrUpdate(Account account);

    SessionManager getSessionManager();
}
