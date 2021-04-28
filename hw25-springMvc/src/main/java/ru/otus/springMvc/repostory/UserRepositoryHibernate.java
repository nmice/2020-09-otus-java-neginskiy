package ru.otus.springMvc.repostory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.springMvc.exception.DaoException;
import ru.otus.springMvc.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.springMvc.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.springMvc.domain.User;
import ru.otus.springMvc.sessionmanager.SessionManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryHibernate implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserRepositoryHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(Long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().get(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }


    @Override
    public Long insert(User entity) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(entity);
            hibernateSession.flush();
            return entity.getId();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(User entity) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(entity);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Long insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() != null) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
            return user.getId();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Criteria criteria = currentSession.getHibernateSession().createCriteria(User.class);
            criteria.add(Restrictions.eq("login", login));
            return Optional.ofNullable((User) criteria.uniqueResult());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return currentSession.getHibernateSession().createCriteria(User.class).list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
