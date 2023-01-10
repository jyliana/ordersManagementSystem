package com.example.system.repository;

import com.example.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("rideRepository")
public class UserRepositoryImpl implements UserRepository {

    @Override
    public List<User> getUsers() {
        User user=new User();
        user.setId(1);
        user.setName("Inna");
        List<User> users = new ArrayList<>();
        users.add(user);
        return users;
    }
}
