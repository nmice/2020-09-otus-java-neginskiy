package ru.otus.webserver.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.webserver.model.User;
import ru.otus.webserver.service.db.DBServiceUser;

import java.io.BufferedReader;
import java.io.IOException;


public class UsersApiServlet extends HttpServlet {

    private final DBServiceUser userService;
    private final Gson gson;

    public UsersApiServlet(DBServiceUser userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try(BufferedReader reader = request.getReader()) {
            var user = gson.fromJson(reader.readLine(), User.class);
            userService.saveUser(user);
        }
    }
}
