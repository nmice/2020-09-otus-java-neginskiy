package ru.otus.springMvc.repostory;

import ru.otus.springMvc.domain.User;
import ru.otus.springMvc.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);

    Long insert(User user);

    void update(User user);

    Long insertOrUpdate(User user);

    Optional<User> findByLogin(String login);

    List<User> findAll();

    SessionManager getSessionManager();
}
