package ru.otus.customorm.service;

import ru.otus.customorm.model.Client;

import java.util.Optional;

/**
 * Сервис для работы c БД для сущности Client.
 */
public interface DBServiceClient {

    long saveEntity(Client entity);

    Optional<Client> getEntityById(long id);
}
