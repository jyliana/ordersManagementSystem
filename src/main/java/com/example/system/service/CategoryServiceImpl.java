package com.example.system.service;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Category;
import com.example.system.repository.CategoryJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.system.service.constants.Constants.CANNOT_BE_CREATED;
import static com.example.system.service.constants.Constants.DOES_NOT_EXIST;

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
            Integer id = categoryJpaRepository.createCategory(category.getName());
            return getCategory(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("The category " + category.getName() + CANNOT_BE_CREATED);
        }
    }

    @Override
    public Category getCategory(Integer id) {
        Category category = categoryJpaRepository.getCategory(id);
        if (null == category) {
            throw new ResourceNotFoundException("The category with id " + id + DOES_NOT_EXIST);
        } else {
            return category;
        }
    }

    @Override
    public List<Map<Object, Object>> getCategoriesSortedByOrderAmount() {
        return categoryJpaRepository.getCategoriesSortedByOrderAmount();
    }

}
