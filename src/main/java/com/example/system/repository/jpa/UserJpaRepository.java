package com.example.system.repository.jpa;

import com.example.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository("userJpaRepository")
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("SELECT o FROM User o")
    List<User> getUsers();

    @Query(value = "SELECT * FROM USERS u WHERE u.id = ?", nativeQuery = true)
    User getUser(Integer id);

    @Transactional
    @Query(value = "INSERT INTO users (name) VALUES (:name) RETURNING *", nativeQuery = true)
    User createUser(@Param("name") String name);

    @Query(value = "SELECT u.id, u.name FROM users u\n" +
            "LEFT JOIN orders_history h ON u.id=h.user_id\n" +
            "WHERE h.user_id IS NULL", nativeQuery = true)
    List<User> getUsersWithoutOrders();


    @Query(value = "SELECT u.id, u.name, SUM(o.amount) sum FROM orders_history h\n" +
            "JOIN users u ON u.id=h.user_id\n" +
            "JOIN orders o ON o.id=h.order_id\n" +
            "GROUP BY u.id\n" +
            "ORDER BY SUM(o.amount) DESC",
            nativeQuery = true)
    List<Map<String, Object>> getUsersSortedByAmountOfOrders();

}