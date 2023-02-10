package com.example.system.controller;

import com.example.system.model.Category;
import com.example.system.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/updateCategory/{id}")
    public Category updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/categoriesSortedByOrderAmount")
    public List<Map<String, Object>> getCategoriesSortedByOrderAmount() {
        return categoryService.getCategoriesSortedByOrderAmount();
    }

}
