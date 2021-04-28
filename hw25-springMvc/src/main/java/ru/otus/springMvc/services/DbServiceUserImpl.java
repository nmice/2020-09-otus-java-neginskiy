package ru.otus.springMvc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.springMvc.repostory.UserRepository;
import ru.otus.springMvc.exception.DbServiceException;
import ru.otus.springMvc.domain.User;
import ru.otus.springMvc.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserRepository userRepository;

    public DbServiceUserImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userRepository.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Long id = userRepository.insertOrUpdate(user);
                sessionManager.commitSession();
                logger.info("created User: {}", id);
                return id;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try (SessionManager sessionManager = userRepository.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userRepository.findById(id);

                logger.info("User: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (SessionManager sessionManager = userRepository.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userRepository.findByLogin(login);
                logger.info("User: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        try (var sessionManager = userRepository.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return userRepository.findAll();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return new ArrayList<>();
        }
    }
}