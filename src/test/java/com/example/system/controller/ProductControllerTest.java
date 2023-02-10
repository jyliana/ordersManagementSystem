package com.example.system.controller;

import com.example.system.model.Category;
import com.example.system.model.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

    private static final String URL_PORT = "http://localhost:8080/";
    private static Integer createdProductId;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetAllProducts() {
        ResponseEntity<List<Product>> productResponse = restTemplate.exchange(URL_PORT + "products", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        List<Product> products = productResponse.getBody();

        assertSoftly(softly -> {
                    softly.assertThat(productResponse.getStatusCode().is2xxSuccessful()).isTrue();
                    softly.assertThat(products).isNotNull();
                    softly.assertThat(products).isNotEmpty();
                    softly.assertThat(products.get(0)).isExactlyInstanceOf(Product.class);
                    softly.assertThat(products.get(0).getName()).isNotNull().isExactlyInstanceOf(String.class);
                }
        );
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetProductById() {
        Product product = restTemplate.getForObject(URL_PORT + "category/1", Product.class);

        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(product).isNotNull();
                    softly.assertThat(product).isExactlyInstanceOf(Product.class);
                    softly.assertThat(product.getName()).isNotNull().isExactlyInstanceOf(String.class);
                }
        );
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetProductWithCategoriesById() {
        ResponseEntity<Map<Object, List<Category>>> exchange = restTemplate.exchange(URL_PORT + "productWithCategories/1", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        Map<Object, List<Category>> productWithCategories = exchange.getBody();
        Optional<Object> product = productWithCategories.keySet().stream().findFirst();
        List<Category> categories = productWithCategories.values().stream().findFirst().get();

        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(exchange.getStatusCode().is2xxSuccessful()).isTrue();
                    softly.assertThat(productWithCategories).isNotNull();
                    softly.assertThat(categories).isNotNull();
                    softly.assertThat(categories.size()).isGreaterThanOrEqualTo(1);
                    softly.assertThat(categories.get(0)).isExactlyInstanceOf(Category.class);
                }
        );
    }

    @Disabled("In order to not create new products constantly")
    @Test
    @Order(1)
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testCreateProduct() {
        Map<String, Object> product = Map.of("name", "Test product", "categories", List.of(Map.of("id", 1)));

        Product result = restTemplate.postForObject(URL_PORT + "product", product, Product.class);

        createdProductId = result.getId();
        assertThat(createdProductId).isNotNull();
        assertThat(result.getName()).isEqualTo("Test product");
    }

    @Disabled("Should work after enabling the test for creating products")
    @Test
    @Order(2)
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testUpdateProduct() {
        Map<String, Object> product = Map.of("name", "New test product", "categories", List.of(Map.of("id", 2)));
        Map<Object, List<Map<String, Object>>> updatedProduct = restTemplate.postForObject(URL_PORT + "updateProduct/" + createdProductId, product, Map.class);

        boolean isNameUpdated = updatedProduct.keySet().stream().allMatch(key -> {
            String p = key.toString();
            return p.contains("New test product") && p.contains(String.valueOf(createdProductId));
        });

        Integer updatedCategory = updatedProduct.values().stream().map(i -> (Integer) i.get(0).get("id")).findFirst().get();

        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(updatedProduct).isNotNull();
                    softly.assertThat(isNameUpdated).isTrue();
                    softly.assertThat(updatedCategory).isEqualTo(2);
                }
        );
    }

    @Disabled("Should work after enabling the test for creating products")
    @Test
    @Order(3)
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testDeleteProduct() {
        ResponseEntity<String> result = restTemplate.exchange(URL_PORT + "deleteProduct/" + createdProductId, HttpMethod.DELETE,
                null,
                String.class);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).contains("has been deleted");
    }

}
