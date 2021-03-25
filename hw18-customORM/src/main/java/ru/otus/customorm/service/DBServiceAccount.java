package ru.otus.customorm.service;

import ru.otus.customorm.model.Account;

import java.util.Optional;

/**
 * Сервис для работы c БД для сущности Account.
 */
public interface DBServiceAccount {

    String saveEntity(Account entity);

    Optional<Account> getEntityById(String id);
}
