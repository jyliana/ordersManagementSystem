package com.example.system.service;

import com.example.system.model.Category;
import com.example.system.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> getProducts();

    Product createProduct(Map<String, Object> product);

    Product getProduct(Integer id);

    Map<Product, List<Category>> getProductWithCategories(Integer id);

    Map<Product, List<Category>> updateProduct(Integer id, Map<String, Object> product);

    String deleteProduct(Integer id);

    List<Product> getAvailableProducts();

    List<Product> getUnAvailableProducts();

    List<Product> unbookProducts();
}
