package ru.otus.webserver.service.auth;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
