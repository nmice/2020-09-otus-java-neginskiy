package ru.otus.customorm.service;

import ru.otus.customorm.model.Client;

import java.util.Optional;

/**
 * Сервис для работы c БД для сущности Client.
 */
public interface DBServiceClient {

    long saveClient(Client entity);

    Optional<Client> getClientById(long id);
}
