package com.example.system.service.impl;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.User;
import com.example.system.repository.jpa.UserJpaRepository;
import com.example.system.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.system.service.constants.Constants.CANNOT_BE_CREATED;
import static com.example.system.service.constants.Constants.DOES_NOT_EXIST;

@Service("userService")
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserJpaRepository userJpaRepository;

    @Override
    public List<User> getUsers() {
        return userJpaRepository.getUsers();
    }

    @Override
    public User createUser(User user) {
        try {
            return userJpaRepository.createUser(user.getName());
        } catch (Exception e) {
            throw new ResourceNotFoundException("The user " + user.getName() + CANNOT_BE_CREATED);
        }
    }

    @Override
    public User getUser(Integer id) {
        User user = userJpaRepository.getUser(id);
        if (null == user) {
            throw new ResourceNotFoundException("A user with id " + id + DOES_NOT_EXIST);
        } else {
            return user;
        }
    }

    @Override
    public List<User> getUsersWithoutOrders() {
        return userJpaRepository.getUsersWithoutOrders();
    }

    @Override
    public Map<User, BigInteger> getUsersSortedByAmountOfOrders() {
        List<Map<String, Object>> maps = userJpaRepository.getUsersSortedByAmountOfOrders();
        Map<User, BigInteger> users = new LinkedHashMap<>();
        maps.forEach(row -> {
                    User user = User.builder()
                            .id((Integer) row.get("id"))
                            .name((String) row.get("name"))
                            .build();

                    users.put(user, (BigInteger) row.get("sum"));
                }
        );
        return users;
    }
}
