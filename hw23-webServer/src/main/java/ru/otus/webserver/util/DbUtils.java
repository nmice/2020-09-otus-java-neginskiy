package ru.otus.webserver.util;

import ru.otus.webserver.model.User;
import ru.otus.webserver.service.db.DBServiceUser;

import java.util.List;

public class DbUtils {

    public static void fillDb(DBServiceUser userService) {
        if (userService.findAll().isEmpty()) {
            List<User> users = List.of(
                    new User(1L, "user1", "admin", "admin"),
                    new User(2L, "user2", "login2", "pass2"),
                    new User(3L, "user3", "login3", "pass3"),
                    new User(4L, "user4", "login4", "pass4"),
                    new User(5L, "user5", "login5", "pass5"),
                    new User(5L, "user5", "login5", "pass5")
            );
            users.forEach(userService::saveUser);
        }
    }
}
