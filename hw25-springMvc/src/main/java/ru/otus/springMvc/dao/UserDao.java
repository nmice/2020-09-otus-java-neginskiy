package ru.otus.springMvc.dao;

import ru.otus.webserver.model.User;
import ru.otus.webserver.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(Long id);

    Long insert(User user);

    void update(User user);

    Long insertOrUpdate(User user);

    Optional<User> findByLogin(String login);

    List<User> findAll();

    SessionManager getSessionManager();
}
