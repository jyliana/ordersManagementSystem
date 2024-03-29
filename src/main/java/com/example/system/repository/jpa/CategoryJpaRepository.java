package com.example.system.repository.jpa;

import com.example.system.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository("categoryJpaRepository")
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    @Query("SELECT o FROM Category o")
    List<Category> getCategories();

    @Query(value = "SELECT * FROM categories u WHERE u.id = ?", nativeQuery = true)
    Category getCategory(Integer id);

    @Transactional
    @Query(value = "INSERT INTO categories (name) VALUES (:name) RETURNING *", nativeQuery = true)
    Category createCategory(@Param("name") String name);

    @Query(value = "SELECT c.name,  SUM(od.amount) total FROM order_details od\n" +
            "JOIN products p ON p.id=od.product_id\n" +
            "JOIN product_categories pc ON pc.product_id=p.id\n" +
            "JOIN categories c ON c.id=pc.category_id\n" +
            "GROUP BY c.name\n" +
            "ORDER BY total DESC", nativeQuery = true)
    List<Map<String, Object>> getCategoriesSortedByOrderAmount();

    @Modifying
    @Transactional
    @Query(value = "UPDATE categories SET name=:name WHERE id=:id", nativeQuery = true)
    Integer updateCategory(@Param("id") Integer id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM categories WHERE id=?", nativeQuery = true)
    Integer deleteCategory(Integer id);
}
