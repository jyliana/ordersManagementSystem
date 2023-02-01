package com.example.system.repository.utils;

import com.example.system.model.Order;
import com.example.system.model.enums.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Order.builder()
                .id(rs.getInt("id"))
                .tradeDate(rs.getDate("trade_date"))
                .amount(rs.getInt("amount"))
                .status(Status.valueOf(rs.getString("status")))
                .build();
    }
}
