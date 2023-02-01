package com.example.system.repository;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.User;
import com.example.system.repository.utils.UserRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;


@Repository("userRepository")
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id", new UserRowMapper());
    }

    @Override
    public User getUser(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id=?", new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("A user with id " + id + " does not exist");
        }
    }

    @Override
    public List<User> getUsersWithoutOrders() {
        return jdbcTemplate.query("SELECT u.id, u.name FROM users u\n" +
                "LEFT JOIN orders_history h ON u.id=h.user_id\n" +
                "WHERE h.user_id IS NULL", new UserRowMapper());
    }

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (name) VALUES (?) ", new String[]{"id"});
            ps.setString(1, user.getName());
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKey().intValue();
        return getUser(id);
    }

    @Override
    public List<Map<String, Object>> getUsersSortedByAmountOfOrders() {
        return jdbcTemplate.queryForList(
                "SELECT u.id, u.name, SUM(o.amount) \"sum\" FROM orders_history h\n" +
                        "JOIN users u ON u.id=h.user_id\n" +
                        "JOIN orders o ON o.id=h.order_id\n" +
                        "GROUP BY u.id\n" +
                        "ORDER BY \"sum\" DESC");
    }

}
