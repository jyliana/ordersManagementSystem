package com.example.system.repository.utils;

import com.example.system.model.UserOrder;
import com.example.system.model.enums.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserOrderRowMapper implements RowMapper<UserOrder> {
    @Override
    public UserOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(rs.getInt("user_id"));
        userOrder.setName(rs.getString("name"));
        userOrder.setOrderId(rs.getInt("order_id"));
        userOrder.setTradeDate(rs.getDate("trade_date"));
        userOrder.setAmount(rs.getInt("amount"));
        userOrder.setStatus(Status.valueOf(rs.getString("status")));
        return userOrder;
    }
}
