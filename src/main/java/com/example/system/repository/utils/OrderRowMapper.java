package com.example.system.repository.utils;

import com.example.system.model.Order;
import com.example.system.model.enums.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setTradeDate(rs.getDate("trade_date"));
        order.setAmount(rs.getInt("amount"));
        order.setStatus(Status.valueOf(rs.getString("status")));
        return order;
    }
}
