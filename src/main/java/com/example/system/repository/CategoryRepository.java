package com.example.system.repository;

import com.example.system.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> getCategories();

    Integer createCategory(String category);

    Category getCategory(Integer id);
}
