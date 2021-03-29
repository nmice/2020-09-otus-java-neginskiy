package ru.otus.jpql.service;

import ru.otus.jpql.model.Account;

import java.util.Optional;

/**
 * Сервис для работы c БД для сущности Account.
 */
public interface DBServiceAccount {

    String saveAccount(Account entity);

    Optional<Account> getAccountById(String id);
}
