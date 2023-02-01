package com.example.system.service;

import com.example.system.model.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<Category> getCategories();

    Category createCategory(Category category);

    Category getCategory(Integer id);

    List<Map<Object, Object>> getCategoriesSortedByOrderAmount();
}
