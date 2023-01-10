package com.example.system.service;

import com.example.system.model.User;
import com.example.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public User createUser(User user) {
        return userRepository.createUser(user);
    }
}
