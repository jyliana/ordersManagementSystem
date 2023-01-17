package com.example.system.repository;

import com.example.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("SELECT o FROM User o")
    List<User> getUsers();

    @Query(value = "SELECT * FROM USERS u WHERE u.id = ?", nativeQuery = true)
    User getUser(Integer id);
}