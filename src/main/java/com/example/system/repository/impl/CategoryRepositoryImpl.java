package com.example.system.repository.impl;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Category;
import com.example.system.repository.CategoryRepository;
import com.example.system.repository.utils.CategoryRowMapper;
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
import static com.example.system.service.constants.Constants.THE_CATEGORY_WITH_ID;


@Repository("categoryRepository")
@AllArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> getCategories() {
        return jdbcTemplate.query("SELECT * FROM categories ORDER BY id", new CategoryRowMapper());
    }

    @Override
    public Category getCategory(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM categories WHERE id=?", new CategoryRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(THE_CATEGORY_WITH_ID + id + DOES_NOT_EXIST);
        }
    }

    @Override
    public Integer createCategory(String category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO categories (name) VALUES (?) ", new String[]{"id"});
            ps.setString(1, category);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public Integer updateCategory(Integer id, String name) {
        return jdbcTemplate.update("UPDATE categories SET name=? WHERE id=?", name, id);
    }

    @Override
    public Integer deleteCategory(Integer id) {
        return jdbcTemplate.update("DELETE FROM categories WHERE id=?", id);
    }

    @Override
    public List<Map<String, Object>> getCategoriesSortedByOrderAmount() {
        return jdbcTemplate.queryForList("SELECT c.name,  SUM(od.amount) total FROM order_details od\n" +
                "JOIN products p ON p.id=od.product_id\n" +
                "JOIN product_categories pc ON pc.product_id=p.id\n" +
                "JOIN categories c ON c.id=pc.category_id\n" +
                "GROUP BY c.name\n" +
                "ORDER BY total DESC");
    }
}
