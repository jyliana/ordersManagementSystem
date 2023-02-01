package com.example.system.repository;

import com.example.system.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getProducts();

    Integer createProduct(String product);

    Product getProduct(Integer id);
}
