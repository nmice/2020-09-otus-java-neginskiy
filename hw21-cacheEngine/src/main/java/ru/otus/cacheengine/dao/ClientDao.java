package ru.otus.cacheengine.dao;

import ru.otus.cacheengine.model.Client;
import ru.otus.cacheengine.sessionmanager.SessionManager;

import java.util.Optional;

public interface ClientDao {
    Optional<Client> findById(long id);
    //List<Client> findAll();

    long insert(Client client);

    void update(Client client);

    long insertOrUpdate(Client client);

    SessionManager getSessionManager();
}
