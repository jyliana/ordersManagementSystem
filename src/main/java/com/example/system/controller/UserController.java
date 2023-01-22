package com.example.system.controller;

import com.example.system.model.User;
import com.example.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    // All users without orders
    @GetMapping("/usersWithoutOrders")
    public List<User> getAllUsersWithoutOrders() {
        return userService.getUsersWithoutOrders();
    }

    // // User’s sorted by sum of amounts of all orders
    @GetMapping("/usersSortedByAmountOfOrders")
    public List<Map<String, Object>> getUsersSortedByAmountOfOrders() {
        return userService.getUsersSortedByAmountOfOrders();
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
