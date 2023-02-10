package com.example.system.controller;

import com.example.system.model.Category;
import com.example.system.model.Product;
import com.example.system.service.ProductService;
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
public class ProductController {

    private ProductService productService;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getProducts();
    }

    @GetMapping("/product/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.getProduct(id);
    }

    @GetMapping("/productWithCategories/{id}")
    public Map<Product, List<Category>> getProductWithCategoriesById(@PathVariable Integer id) {
        return productService.getProductWithCategories(id);
    }

    @PostMapping("/product")
    public Product createProduct(@RequestBody Map<String, Object> product) {
        return productService.createProduct(product);
    }

    @PostMapping("/updateProduct/{id}")
    public Map<Product, List<Category>> updateProduct(@PathVariable Integer id, @RequestBody Map<String, Object> product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }

}
