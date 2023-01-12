package com.example.system.controller;

import com.example.system.model.User;
import com.example.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public @ResponseBody List<User> getAllUsers() {
        return userService.getUsers();
    }

    // All users without orders
    @GetMapping("/usersWithoutOrders")
    public @ResponseBody List<User> getAllUsersWithoutOrders() {
        return userService.getUsersWithoutOrders();
    }

    // // User’s sorted by sum of amounts of all orders
    @GetMapping("/usersSortedByAmountOfOrders")
    public @ResponseBody Map<User, Long> getUsersSortedByAmountOfOrders() {
        return userService.getUsersSortedByAmountOfOrders();
    }

    @PostMapping("/user")
    public @ResponseBody User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }
}
