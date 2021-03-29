package ru.otus.jpql.dao;


import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jpql.exception.ClientDaoException;
import ru.otus.jpql.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.jpql.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.jpql.model.Account;
import ru.otus.jpql.sessionmanager.SessionManager;

import java.util.Optional;

public class AccountDaoHibernate implements AccountDao {
    private static final Logger logger = LoggerFactory.getLogger(AccountDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public AccountDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Account> findById(String id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(Account.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public String insert(Account account) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(account);
            hibernateSession.flush();
            return account.getNo();
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public void update(Account account) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(account);
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public String insertOrUpdate(Account account) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (account.getNo() != null && !account.getNo().isEmpty()) {
                hibernateSession.merge(account);
            } else {
                hibernateSession.persist(account);
                hibernateSession.flush();
            }
            return account.getNo();
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
