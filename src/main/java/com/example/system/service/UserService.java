package com.example.system.service;

import com.example.system.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> getUsers();

    User createUser(User user);

    User getUser(Integer id);

    List<User> getUsersWithoutOrders();

    Map<User, Long> getUsersSortedByAmountOfOrders();
}
