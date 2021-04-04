package ru.otus.cacheengine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cacheengine.cachehw.HwCache;
import ru.otus.cacheengine.cachehw.MyCache;
import ru.otus.cacheengine.dao.ClientDao;
import ru.otus.cacheengine.exception.DbServiceException;
import ru.otus.cacheengine.model.Client;
import ru.otus.cacheengine.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientDao clientDao;

    public DbServiceClientImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public long saveClient(Client client) {
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long clientId = clientDao.insertOrUpdate(client);
                sessionManager.commitSession();
                logger.info("created client: {}", clientId);
                return clientId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Client> getClientById(long id) {
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Client> clientOptional = clientDao.findById(id);

                logger.info("client: {}", clientOptional.orElse(null));
                return clientOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}