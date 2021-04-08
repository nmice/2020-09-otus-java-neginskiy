package ru.otus.webserver.service;

import ru.otus.webserver.model.Client;

import java.util.Optional;

/**
 * Сервис для работы c БД для сущности Client.
 */
public interface DBServiceClient {

    long saveClient(Client entity);

    Optional<Client> getClientById(long id);
}
