package com.example.system.repository;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Order;
import com.example.system.model.User;
import com.example.system.model.enums.Status;
import com.example.system.repository.utils.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                        "WHERE u.id=?", new Object[]{id}, Integer.class);
    }

    @Override
    public Map<User, List<Order>> getUsersWithOrders() {
        String sql = "SELECT u.id, u.name, o.id \"order_id\", o.trade_date, o.amount, o.status FROM orders_history h\n" +
                "JOIN users u ON u.id=h.user_id\n" +
                "JOIN orders o ON o.id=h.order_id\n" +
                "ORDER BY o.trade_date";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);

        Map<User, List<Order>> result = new LinkedHashMap<>();

        maps.stream().map(row -> {
                    User user = new User();
                    user.setId((Integer) row.get("id"));
                    user.setName((String) row.get("name"));

                    Order order = new Order();
                    order.setId((Integer) row.get("order_id"));
                    order.setTradeDate((Date) row.get("trade_date"));
                    order.setAmount((Integer) row.get("amount"));
                    order.setStatus(Status.valueOf(row.get("status").toString()));
                    if (null == result.get(user)) {
                        result.put(user, List.of(order));
                    } else {
                        List<Order> orders = new ArrayList<>(result.get(user));
                        orders.add(order);
                        result.put(user, orders);
                    }
            return result;
                }
        ).collect(Collectors.toList());
        return result;
    }
}
