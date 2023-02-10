package com.example.system.service;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Category;
import com.example.system.model.Product;
import com.example.system.repository.ProductRepository;
import com.example.system.repository.jpa.CategoryJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.system.service.constants.Constants.CANNOT_BE_CREATED;
import static com.example.system.service.constants.Constants.DOES_NOT_EXIST;
import static com.example.system.service.constants.Constants.THE_PRODUCT_WITH_ID;

@Service("productService")
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    @Override
    public Product createProduct(Map<String, Object> product) {
        String name = (String) product.get("name");
        try {
            List<Integer> categoryIds = getCategoryIds(product);
            if (allCategoriesExist(categoryIds)) {
                Integer id = productRepository.createProduct(name);
                categoryIds.forEach(categoryId -> productRepository.updateProductCategory(id, categoryId));
                return getProduct(id);
            } else throw new ResourceNotFoundException("Something happened.");
        } catch (Exception e) {
            throw new ResourceNotFoundException("The product " + name + CANNOT_BE_CREATED);
        }
    }

    @Override
    public Product getProduct(Integer id) {
        Product product = productRepository.getProduct(id);
        if (null == product) {
            throw new ResourceNotFoundException(THE_PRODUCT_WITH_ID + id + DOES_NOT_EXIST);
        } else {
            return product;
        }
    }

    @Override
    public Map<Product, List<Category>> updateProduct(Integer id, Map<String, Object> product) {
        String name = (String) product.get("name");
        List<Integer> categoryIds = getCategoryIds(product);

        if (!categoryIds.isEmpty() && allCategoriesExist(categoryIds)) {
            productRepository.deleteFromProductCategory(id);
            categoryIds.forEach(categoryId -> productRepository.updateProductCategory(id, categoryId));
        }

        if (productRepository.updateProduct(id, name) != 1) {
            throw new ResourceNotFoundException(THE_PRODUCT_WITH_ID + id + DOES_NOT_EXIST);
        }
        return getProductWithCategories(id);
    }


    @Override
    public Map<Product, List<Category>> getProductWithCategories(Integer id) {
        List<Map<String, Object>> productWithCategories = productRepository.getProductWithCategories(id);
        if (productWithCategories.isEmpty()) {
            throw new ResourceNotFoundException(THE_PRODUCT_WITH_ID + id + DOES_NOT_EXIST);
        }
        Map<String, Object> productMap = productWithCategories.get(0);
        Product product = Product.builder()
                .id((Integer) productMap.get("product_id"))
                .name((String) productMap.get("product_name"))
                .build();
        List<Category> categories = productWithCategories.stream().map(row -> Category.builder()
                .id((Integer) row.get("category_id"))
                .name((String) row.get("category_name"))
                .build()
        ).collect(Collectors.toList());

        return Map.of(product, categories);
    }

    @Override
    public String deleteProduct(Integer id) {
        if (productRepository.deleteProduct(id) != 1) {
            throw new ResourceNotFoundException(THE_PRODUCT_WITH_ID + id + DOES_NOT_EXIST);
        }
        return THE_PRODUCT_WITH_ID + id + " has been deleted.";
    }

    private static List<Integer> getCategoryIds(Map<String, Object> product) {
        return ((List<Map<String, Object>>) product.get("categories")).stream()
                .map(item -> (Integer) item.get("id")).collect(Collectors.toList());
    }

    private boolean allCategoriesExist(List<Integer> categoryIds) {
        return categoryIds.stream().allMatch(categoryId -> null != categoryJpaRepository.getCategory(categoryId));
    }
}
