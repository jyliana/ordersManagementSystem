package com.example.system.repository;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Order;
import com.example.system.model.UserOrder;
import com.example.system.repository.utils.OrderRowMapper;
import com.example.system.repository.utils.UserOrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository("orderRepository")
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Order> getOrders() {
        return jdbcTemplate.query("SELECT * FROM orders ORDER BY id", new OrderRowMapper());
    }

    @Override
    public Order createOrder(Integer id, Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO orders (trade_date, amount, status) VALUES (?, ?, ?::order_status)", new String[]{"id"});
            ps.setDate(1, order.getTradeDate());
            ps.setInt(2, order.getAmount());
            ps.setString(3, order.getStatus().name());
            return ps;
        }, keyHolder);

        int orderId = keyHolder.getKey().intValue();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO orders_history (user_id, order_id) VALUES (?, ?)", new String[]{"id"});
            ps.setInt(1, id);
            ps.setInt(2, orderId);
            return ps;
        });

        return getOrder(orderId);
    }

    @Override
    public Order getOrder(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM orders WHERE id=?", new OrderRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("An order with id " + id + " does not exist");
        }
    }

    @Override
    public Order deleteOrder(Integer id) {
        int update = jdbcTemplate.update("UPDATE orders SET status = 'DELETED' WHERE id=?", id);
        if (update != 1) {
            throw new ResourceNotFoundException("An order with id " + id + " does not exist");
        }
        return getOrder(id);
    }

    @Override
    public List<Order> getValidSortedOrdersByUserId(Integer id) {
        return jdbcTemplate.query(
                "SELECT o.id, o.trade_date, o.amount, o.status FROM orders_history h\n" +
                        "JOIN users u ON u.id=h.user_id\n" +
                        "JOIN orders o ON o.id=h.order_id\n" +
                        "WHERE u.id=? AND o.status='VALID'\n" +
                        "ORDER BY o.trade_date",
                new OrderRowMapper(), id);
    }

    @Override
    public Integer getSumOfAllOrdersByUserId(Integer id) {
        return jdbcTemplate.queryForObject(
                "SELECT SUM(o.amount) FROM orders_history h\n" +
                        "JOIN users u ON u.id=h.user_id\n" +
                        "JOIN orders o ON o.id=h.order_id\n" +
                        "WHERE u.id=?", Integer.class, id);
    }

    @Override
    public List<UserOrder> getUsersWithOrdersWithStatus(String status) {
        try {
            return jdbcTemplate.query("SELECT u.id \"user_id\", u.name, o.id \"order_id\", o.trade_date, o.amount, o.status FROM orders_history h\n" +
                    "JOIN users u ON u.id=h.user_id\n" +
                    "JOIN orders o ON o.id=h.order_id\n" +
                    "where o.status=?::order_status", new UserOrderRowMapper(), status.toUpperCase());
        } catch (DataIntegrityViolationException e) {
            throw new ResourceNotFoundException("The status " + status + " does not exist");
        }
    }
}
