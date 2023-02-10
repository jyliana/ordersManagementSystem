package com.example.system.service;

import com.example.system.model.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<Category> getCategories();

    Category createCategory(Category category);

    Category getCategory(Integer id);

    List<Map<String, Object>> getCategoriesSortedByOrderAmount();

    Category updateCategory(Integer id, Category category);

    String deleteCategory(Integer id);
}
