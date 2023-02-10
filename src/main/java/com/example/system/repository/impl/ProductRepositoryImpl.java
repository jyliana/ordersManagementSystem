package com.example.system.repository.impl;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Product;
import com.example.system.repository.ProductRepository;
import com.example.system.repository.utils.ProductRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import static com.example.system.service.constants.Constants.DOES_NOT_EXIST;
import static com.example.system.service.constants.Constants.THE_PRODUCT_WITH_ID;


@Repository("productRepository")
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Product> getProducts() {
        return jdbcTemplate.query("SELECT * FROM products ORDER BY id", new ProductRowMapper());
    }

    @Override
    public Product getProduct(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM products WHERE id=?", new ProductRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(THE_PRODUCT_WITH_ID + id + DOES_NOT_EXIST);
        }
    }

    @Override
    public Integer createProduct(String category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO products (name) VALUES (?)", new String[]{"id"});
            ps.setString(1, category);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public Integer updateProduct(Integer id, String name) {
        return jdbcTemplate.update("UPDATE products SET name=? WHERE id=?", name, id);
    }

    @Override
    public Integer deleteProduct(Integer id) {
        return jdbcTemplate.update("DELETE FROM products WHERE id=?", id);
    }

    @Override
    public void updateProductCategory(Integer userId, Integer orderId) {
        jdbcTemplate.update("INSERT INTO product_categories (product_id, category_id) VALUES (?, ?)", userId, orderId);
    }

    @Override
    public List<Map<String, Object>> getProductWithCategories(Integer id) {
        return jdbcTemplate.queryForList("SELECT c.id \"category_id\", c.name \"category_name\", p.id \"product_id\", p.name \"product_name\" " +
                "FROM products p\n" +
                "JOIN product_categories pc ON pc.product_id=p.id\n" +
                "JOIN categories c ON c.id=pc.category_id\n" +
                "WHERE p.id=?", id);
    }

    @Override
    public Integer deleteFromProductCategory(Integer id) {
        return jdbcTemplate.update("DELETE FROM product_categories WHERE product_id=?", id);
    }
}
