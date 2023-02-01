package com.example.system.service;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Product;
import com.example.system.repository.CategoryJpaRepository;
import com.example.system.repository.ProductJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.system.service.constants.Constants.CANNOT_BE_CREATED;
import static com.example.system.service.constants.Constants.DOES_NOT_EXIST;

@Service("productService")
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductJpaRepository productJpaRepository;
    private CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Product> getProducts() {
        return productJpaRepository.getProducts();
    }

    @Override
    public Product createProduct(Map<String, Object> product) {
        String name = (String) product.get("name");
        try {
            List<Integer> categoryIds = ((List<Map<String, Object>>) product.get("categories")).stream()
                    .map(item -> (Integer) item.get("id")).collect(Collectors.toList());

            boolean allCategoriesExist = categoryIds.stream().allMatch(categoryId -> null != categoryJpaRepository.getCategory(categoryId));
            if (allCategoriesExist) {
                Integer id = productJpaRepository.createProduct(name);
                categoryIds.forEach(categoryId -> productJpaRepository.updateProductCategory(id, categoryId));
                return getProduct(id);
            } else throw new Exception();
        } catch (Exception e) {
            throw new ResourceNotFoundException("The product " + name + CANNOT_BE_CREATED);
        }
    }

    @Override
    public Product getProduct(Integer id) {
        Product product = productJpaRepository.getProduct(id);
        if (null == product) {
            throw new ResourceNotFoundException("A product with id " + id + DOES_NOT_EXIST);
        } else {
            return product;
        }
    }

}
