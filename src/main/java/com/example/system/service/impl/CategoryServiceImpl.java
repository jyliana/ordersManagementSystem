package com.example.system.service.impl;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Category;
import com.example.system.repository.jpa.CategoryJpaRepository;
import com.example.system.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.system.service.constants.Constants.CANNOT_BE_CREATED;
import static com.example.system.service.constants.Constants.DOES_NOT_EXIST;
import static com.example.system.service.constants.Constants.THE_CATEGORY_WITH_ID;

@Service("categoryService")
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Category> getCategories() {
        return categoryJpaRepository.getCategories();
    }

    @Override
    public Category createCategory(Category category) {
        try {
            return categoryJpaRepository.createCategory(category.getName());
        } catch (Exception e) {
            throw new ResourceNotFoundException("The category " + category.getName() + CANNOT_BE_CREATED);
        }
    }

    @Override
    public Category getCategory(Integer id) {
        Category category = categoryJpaRepository.getCategory(id);
        if (null == category) {
            throw new ResourceNotFoundException(THE_CATEGORY_WITH_ID + id + DOES_NOT_EXIST);
        } else {
            return category;
        }
    }

    @Override
    public List<Map<String, Object>> getCategoriesSortedByOrderAmount() {
        return categoryJpaRepository.getCategoriesSortedByOrderAmount();
    }

    @Override
    public Category updateCategory(Integer id, Category category) {
        if (categoryJpaRepository.updateCategory(id, category.getName()) != 1) {
            throw new ResourceNotFoundException(THE_CATEGORY_WITH_ID + id + DOES_NOT_EXIST);
        }
        return getCategory(id);
    }

    @Override
    public String deleteCategory(Integer id) {
        if (categoryJpaRepository.deleteCategory(id) != 1) {
            throw new ResourceNotFoundException(THE_CATEGORY_WITH_ID + id + DOES_NOT_EXIST);
        }
        return THE_CATEGORY_WITH_ID + id + " has been deleted.";
    }

}
