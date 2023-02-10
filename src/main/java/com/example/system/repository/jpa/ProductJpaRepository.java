package com.example.system.repository.jpa;

import com.example.system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository("productJpaRepository")
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

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
    void updateProductCategory(Integer userId, Integer orderId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET name=:name WHERE id=:id", nativeQuery = true)
    Integer updateProduct(@Param("id") Integer id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM products WHERE id=?", nativeQuery = true)
    Integer deleteProduct(Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product_categories WHERE product_id=?", nativeQuery = true)
    Integer deleteFromProductCategory(Integer id);

    @Query(value = "SELECT c.id \"category_id\", c.name \"category_name\", p.id \"product_id\", p.name \"product_name\" " +
            "FROM products p\n" +
            "JOIN product_categories pc ON pc.product_id=p.id\n" +
            "JOIN categories c ON c.id=pc.category_id\n" +
            "WHERE p.id=?", nativeQuery = true)
    List<Map<String, Object>> getProductWithCategories(Integer id);
}