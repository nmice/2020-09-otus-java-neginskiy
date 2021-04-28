package ru.otus.springMvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.springMvc.domain.User;
import ru.otus.springMvc.services.DBServiceUser;

import java.util.List;

@Controller
public class UserController {

    private final DBServiceUser userService;

    public UserController(DBServiceUser userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/user/list"})
    public String usersListView(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("user", new User());
        return "usersList.html";
    }

    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute User user) {
        userService.saveUser(user);
        return new RedirectView("/", true);
    }
}
