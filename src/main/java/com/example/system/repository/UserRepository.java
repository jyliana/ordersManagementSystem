package com.example.system.repository;

import com.example.system.model.User;

import java.util.List;
import java.util.Map;

public interface UserRepository {
    List<User> getUsers();

    User createUser(User user);

    User getUser(Integer id);

    List<User> getUsersWithoutOrders();

    Map<User, Long> getUsersSortedByAmountOfOrders();
}
