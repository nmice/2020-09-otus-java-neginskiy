package ru.otus.webserver.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.webserver.model.User;
import ru.otus.webserver.processor.TemplateProcessor;
import ru.otus.webserver.service.db.DBServiceUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_USERS = "users";

    private final DBServiceUser userService;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, DBServiceUser userService) {
        this.templateProcessor = templateProcessor;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String mode = extractModeFromRequest(req);

        Map<String, Object> paramsMap = new HashMap<>();
        List<User> users = new ArrayList<>();
        if ("ALL".equals(mode)) {
            users = userService.findAll();
        }
        paramsMap.put(TEMPLATE_USERS, users);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    private String extractModeFromRequest(HttpServletRequest request) {
        return request.getParameter("mode");
    }
}
