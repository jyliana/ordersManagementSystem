package com.example.system.repository;

import com.example.system.model.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderJpaRepository extends JpaRepository<UserOrder, Long> {

    @Query(value = "SELECT u.id \"user_id\", u.name, o.id \"order_id\", o.trade_date, o.amount, o.status FROM orders_history h\n" +
            "JOIN users u ON u.id=h.user_id\n" +
            "JOIN orders o ON o.id=h.order_id\n" +
            "ORDER BY o.trade_date", nativeQuery = true)
    List<UserOrder> getUsersWithOrders();
}
