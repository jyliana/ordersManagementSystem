package com.example.system.repository;

import com.example.system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("productJpaRepository")
public interface ProductJpaRepository extends JpaRepository<Product, Long>, ProductRepository {

    @Query("SELECT o FROM Product o")
    List<Product> getProducts();

    @Query(value = "SELECT * FROM products u WHERE u.id = ?", nativeQuery = true)
    Product getProduct(Integer id);

    @Transactional
    @Query(value = "INSERT INTO products (name) VALUES (:name) RETURNING id", nativeQuery = true)
    Integer createProduct(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO product_categories (product_id, category_id) VALUES (?, ?)", nativeQuery = true)
    Integer updateProductCategory(Integer userId, Integer orderId);

}