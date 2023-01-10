package com.example.system.service;

import com.example.system.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User createUser(User user);
}
