package com.example.system.repository;

import com.example.system.model.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o")
    List<Order> getOrders(Sort id);

    @Query(value = "SELECT * FROM ORDERS WHERE id = ?", nativeQuery = true)
    Order getOrder(Integer id);

    @Query(value = "SELECT u.id, u.name, o.id \"order_id\", o.trade_date, o.amount, o.status FROM orders_history h\n" +
            "JOIN users u ON u.id=h.user_id\n" +
            "JOIN orders o ON o.id=h.order_id\n" +
            "ORDER BY o.trade_date", nativeQuery = true)
    List<Map<String, Object>> getUsersWithOrders();
}