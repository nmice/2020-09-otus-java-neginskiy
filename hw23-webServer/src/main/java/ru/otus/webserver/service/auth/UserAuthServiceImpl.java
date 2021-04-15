package ru.otus.webserver.service.auth;

import ru.otus.webserver.service.db.DBServiceUser;

public class UserAuthServiceImpl implements UserAuthService {

    private final DBServiceUser userService;

    public UserAuthServiceImpl(DBServiceUser userService) {
        this.userService = userService;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return userService.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
