package com.example.system.repository;

import com.example.system.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();
}
