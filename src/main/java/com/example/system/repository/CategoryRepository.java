package com.example.system.repository;

import com.example.system.model.Category;

import java.util.List;
import java.util.Map;

public interface CategoryRepository {
    List<Category> getCategories();

    Integer createCategory(String category);

    Category getCategory(Integer id);

    Integer updateCategory(Integer id, String name);

    Integer deleteCategory(Integer id);

    List<Map<String, Object>> getCategoriesSortedByOrderAmount();
}
