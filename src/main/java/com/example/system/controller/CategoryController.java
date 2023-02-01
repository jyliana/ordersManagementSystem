package com.example.system.controller;

import com.example.system.model.Category;
import com.example.system.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/category/{id}")
    public Category getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategory(id);
    }

    @PostMapping("/category")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping("/categoriesSortedByOrderAmount")
    public List<Map<Object, Object>> getCategoriesSortedByOrderAmount() {
        return categoryService.getCategoriesSortedByOrderAmount();
    }

}
