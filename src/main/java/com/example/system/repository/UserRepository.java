package com.example.system.repository;

import com.example.system.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();

    User createUser(User user);

    User getUser(Integer id);
}
