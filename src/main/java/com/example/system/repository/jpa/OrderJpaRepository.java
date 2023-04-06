package com.example.system.repository.jpa;

import com.example.system.model.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o")
    List<Order> getOrders(Sort id);

    @Query(value = "SELECT * FROM ORDERS WHERE id = ?", nativeQuery = true)
    Order getOrder(Integer id);


    @Transactional
    @Query(value = "INSERT INTO orders (amount, status) VALUES (?, ?) RETURNING id", nativeQuery = true)
    Integer createOrder(@Param("amount") Integer amount, @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO order_details (order_id, product_id, amount) values (?, ?, ?)", nativeQuery = true)
    Integer updateOrderDetails(Integer orderId, Integer productId, Integer amount);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO orders_history (user_id, order_id) VALUES (?, ?)", nativeQuery = true)
    Integer updateOrdersHistory(Integer userId, Integer orderId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE orders SET status = 'DELETED' WHERE id=?", nativeQuery = true)
    Integer deleteOrder(Integer id);

    @Query(value = "SELECT u.id \"user_id\", u.name, o.id \"order_id\", o.trade_date, o.amount, " +
            "o.status, p.id \"product_id\", p.name \"product\", od.amount \"quantity\" \n" +
            "FROM orders_history h\n" +
            "JOIN users u ON u.id=h.user_id\n" +
            "JOIN orders o ON o.id=h.order_id\n" +
            "JOIN order_details od ON od.order_id=o.id\n" +
            "JOIN products p ON p.id=od.product_id\n" +
            "ORDER BY o.trade_date", nativeQuery = true)
    List<Map<String, Object>> getUsersWithOrders();

    @Query(value = "SELECT SUM(o.amount) FROM orders_history h\n" +
            "JOIN users u ON u.id=h.user_id\n" +
            "JOIN orders o ON o.id=h.order_id\n" +
            "WHERE u.id=?", nativeQuery = true)
    Integer getSumOfAllOrdersByUserId(Integer id);

    @Query(value = "SELECT o.id, o.trade_date, o.amount, o.status FROM orders_history h\n" +
            "JOIN users u ON u.id=h.user_id\n" +
            "JOIN orders o ON o.id=h.order_id\n" +
            "WHERE u.id=? AND o.status='VALID'\n" +
            "ORDER BY o.trade_date", nativeQuery = true)
    List<Order> getValidSortedOrdersByUserId(Integer id);

    @Query(value = "SELECT u.id \"user_id\", u.name, o.id \"order_id\", o.trade_date, o.amount, o.status, " +
            "p.id \"product_id\", p.name \"product\", od.amount \"quantity\"" +
            "FROM orders_history h\n" +
            "JOIN users u ON u.id=h.user_id\n" +
            "JOIN orders o ON o.id=h.order_id\n" +
            "JOIN order_details od ON od.order_id=o.id\n" +
            "JOIN products p ON p.id=od.product_id\n" +
            "where o.status = :order_status", nativeQuery = true)
    List<Map<String, Object>> getUsersWithOrdersWithStatus(@Param("order_status") String status);

    @Query(value = "SELECT u.id \"user_id\", u.name, o.id \"order_id\", o.trade_date, o.amount, o.status, \n" +
            "p.id \"product_id\", p.name \"product\", od.amount \"quantity\"" +
            "FROM orders_history h\n" +
            "JOIN users u ON u.id=h.user_id\n" +
            "JOIN orders o ON o.id=h.order_id\n" +
            "JOIN order_details od ON od.order_id=o.id\n" +
            "JOIN products p ON p.id=od.product_id\n" +
            "JOIN product_categories pc on pc.product_id=od.product_id\n" +
            "WHERE pc.category_id=(SELECT id FROM categories WHERE name=?)", nativeQuery = true)
    List<Map<String, Object>> getUsersWithOrdersWithProductsFromCategory(String category);

    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET booked_quantity=booked_quantity+:quantity, available_quantity=available_quantity-:quantity WHERE id=:id", nativeQuery = true)
    Integer bookProduct(@Param("id") Integer productId, @Param("quantity") Integer productQuantity);


    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET available_quantity=available_quantity-:quantity WHERE id=:id", nativeQuery = true)
    Integer buyProduct(@Param("id") Integer productId, @Param("quantity") Integer productQuantity);
}
