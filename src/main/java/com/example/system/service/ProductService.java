package com.example.system.service;

import com.example.system.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> getProducts();

    Product createProduct(Map<String, Object> product);

    Product getProduct(Integer id);

}
