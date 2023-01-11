package com.example.system.repository;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.User;
import com.example.system.repository.utils.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;


@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query("select * from users", new UserRowMapper());
    }

    @Override
    public User getUser(Integer id) {
        User user;
        try {
            user = jdbcTemplate.queryForObject("select * from users where id=?", new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("A user with id " + id + " does not exist");
        }
        return user;
    }

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("insert into users (name) values (?) ", new String[]{"id"});
            ps.setString(1, user.getName());
            return ps;
        }, keyHolder);

        Number id = keyHolder.getKey();
        return getUser(id.intValue());
    }

}
