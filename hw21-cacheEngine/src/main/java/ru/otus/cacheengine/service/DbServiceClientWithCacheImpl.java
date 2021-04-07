package ru.otus.cacheengine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cacheengine.cachehw.HwCache;
import ru.otus.cacheengine.dao.ClientDao;
import ru.otus.cacheengine.exception.DbServiceException;
import ru.otus.cacheengine.model.Client;
import ru.otus.cacheengine.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceClientWithCacheImpl implements DBServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientWithCacheImpl.class);

    private final ClientDao clientDao;
    private final HwCache<String, Client> clientDaoCache;

    public DbServiceClientWithCacheImpl(ClientDao clientDao, HwCache<String, Client> clientDaoCache) {
        this.clientDao = clientDao;
        this.clientDaoCache = clientDaoCache;
    }

    @Override
    public long saveClient(Client client) {
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long id = clientDao.insertOrUpdate(client);
                sessionManager.commitSession();
                logger.info("created client: {}", id);
                clientDaoCache.put(String.valueOf(id), client);
                return id;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Client> getClientById(long id) {
        Client cacheClient = clientDaoCache.get(String.valueOf(id));
        if (cacheClient != null) {
            return Optional.of(cacheClient);
        }
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Client> clientOptional = clientDao.findById(id);
                clientOptional.ifPresent(client -> clientDaoCache.put(String.valueOf(id), client));
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