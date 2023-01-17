package com.example.system.service;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.User;
import com.example.system.repository.UserJpaRepository;
import com.example.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public List<User> getUsers() {
        return userJpaRepository.getUsers();
    }

    @Override
    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public User getUser(Integer id) {
        User user = userJpaRepository.getUser(id);
        if (null == user) {
            throw new ResourceNotFoundException("A user with id " + id + " does not exist");
        } else {
            return user;
        }
    }

    @Override
    public List<User> getUsersWithoutOrders() {
        return userRepository.getUsersWithoutOrders();
    }

    @Override
    public Map<User, Long> getUsersSortedByAmountOfOrders() {
        return userRepository.getUsersSortedByAmountOfOrders();
    }
}
