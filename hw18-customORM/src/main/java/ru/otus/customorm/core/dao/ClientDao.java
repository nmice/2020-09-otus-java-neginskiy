package ru.otus.customorm.core.dao;

import ru.otus.customorm.core.model.Client;
import ru.otus.customorm.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface ClientDao {
    Optional<Client> findById(long id);
    //List<Client> findAll();

    long insert(Client client);

    //void update(Client client);
    //long insertOrUpdate(Client client);

    SessionManager getSessionManager();
}
