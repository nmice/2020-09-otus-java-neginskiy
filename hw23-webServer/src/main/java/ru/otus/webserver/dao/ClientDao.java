package ru.otus.webserver.dao;

import ru.otus.webserver.model.Client;
import ru.otus.webserver.sessionmanager.SessionManager;

import java.util.Optional;

public interface ClientDao {
    Optional<Client> findById(long id);
    //List<Client> findAll();

    long insert(Client client);

    void update(Client client);

    long insertOrUpdate(Client client);

    SessionManager getSessionManager();
}
