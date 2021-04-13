package ru.otus.webserver.service.db;

import ru.otus.webserver.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы c БД для сущности User.
 */
public interface DBServiceUser {

    long saveUser(User entity);

    Optional<User> getUserById(Long id);

    Optional<User> findByLogin(String login);

    List<User> findAll();
}
