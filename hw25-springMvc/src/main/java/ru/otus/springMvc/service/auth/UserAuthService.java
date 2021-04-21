package ru.otus.springMvc.service.auth;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
