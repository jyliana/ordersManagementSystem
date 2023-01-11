package com.example.system.repository;

import com.example.system.model.Order;
import com.example.system.repository.utils.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("orderRepository")
public class OrderRepositoryImpl implements OrderRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Order> getOrders() {
        return jdbcTemplate.query("select * from orders", new OrderRowMapper());
    }
}
