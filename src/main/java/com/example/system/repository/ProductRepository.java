package com.example.system.repository;

import com.example.system.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductRepository {
    List<Product> getProducts();

    Integer createProduct(String product);

    Product getProduct(Integer id);

    Integer updateProduct(Integer id, String name);

    Integer deleteProduct(Integer id);

    void updateProductCategory(Integer userId, Integer orderId);

    List<Map<String, Object>> getProductWithCategories(Integer id);

    Integer deleteFromProductCategory(Integer id);
}
