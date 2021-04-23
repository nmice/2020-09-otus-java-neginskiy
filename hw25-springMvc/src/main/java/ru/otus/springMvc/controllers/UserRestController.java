package ru.otus.springMvc.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.springMvc.domain.User;
import ru.otus.springMvc.services.DBServiceUser;

import java.util.List;

@RestController
public class UserRestController {

    private final DBServiceUser userService;

    public UserRestController(DBServiceUser userService) {
        this.userService = userService;
    }

    @GetMapping("/api/user/{id}")
    public User getClientById(@PathVariable(name = "id") long id) {
        return userService.getUserById(id).orElse(null);
    }

    @GetMapping("/api/user")
    public User getUserByLogin(@RequestParam(name = "login") String login) {
        return userService.findByLogin(login).orElse(null);
    }

    @PostMapping("/api/user")
    public Long saveClient(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/users")
    public List<User> getUsers() {
        return userService.findAll();
    }
}
