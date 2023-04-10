package com.example.system.controller;

import com.example.system.model.Category;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryControllerTest {
    private static final String URL_PORT = "http://localhost:8080/";
    private static Integer createdCategoryId;
    private final RestTemplate restTemplate = new RestTemplate();


    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetAllCategories() {
        ResponseEntity<List<Category>> categoryResponse = restTemplate.exchange(URL_PORT + "categories", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        List<Category> categories = categoryResponse.getBody();

        assertSoftly(softly -> {
                    softly.assertThat(categoryResponse.getStatusCode().is2xxSuccessful()).isTrue();
                    softly.assertThat(categories).isNotNull();
                    softly.assertThat(categories).isNotEmpty();
                    softly.assertThat(categories.get(0)).isExactlyInstanceOf(Category.class);
                    softly.assertThat(categories.get(0).getName()).isNotNull().isExactlyInstanceOf(String.class);
                }
        );
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetCategoryById() {
        Category category = restTemplate.getForObject(URL_PORT + "category/1", Category.class);

        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(category).isNotNull();
                    softly.assertThat(category).isExactlyInstanceOf(Category.class);
                    softly.assertThat(category.getName()).isNotNull().isExactlyInstanceOf(String.class);
                }
        );
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetNotExistingCategoryById() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> restTemplate.getForObject(URL_PORT + "category/111111", Category.class));

        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(exception.getRawStatusCode()).isEqualTo(404);
                    softly.assertThat(exception.getMessage().contains("The category with id 111111 does not exist")).isTrue();
                }
        );
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testUpdateCategory() {
        Category category = restTemplate.postForObject(URL_PORT + "updateCategory/1",
                Category.builder()
                        .name("Makeup")
                        .build(),
                Category.class);

        assertThat(category).isNotNull().isExactlyInstanceOf(Category.class);
        assertThat(category.getName()).isEqualTo("Makeup");
    }

    @Disabled("In order to not create new categories constantly")
    @Order(1)
    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testCreateCategory() {
        Category category = Category.builder()
                .name("Test category")
                .build();

        category = restTemplate.postForObject(URL_PORT + "category", category, Category.class);
        createdCategoryId = category.getId();
        assertThat(createdCategoryId).isNotNull();
        assertThat(category.getName()).isEqualTo("Test category");
    }

    @Disabled("Should work after enabling the test for creating categories")
    @Test
    @Order(2)
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testDeleteCategory() {
        ResponseEntity<String> result = restTemplate.exchange(URL_PORT + "deleteCategory/" + createdCategoryId, HttpMethod.DELETE,
                null,
                String.class);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).contains("has been deleted");
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testDeleteNonExistingCategory() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> restTemplate.exchange(URL_PORT + "deleteCategory/11111", HttpMethod.DELETE,
                null,
                String.class));

        assertThat(exception.getStatusCode().is4xxClientError()).isTrue();
        assertThat(exception.getMessage()).contains("The category with id 11111 does not exist");
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetCategoriesSortedByOrderAmount() {
        ResponseEntity<List<Map<Object, Object>>> categoryResponse = restTemplate.exchange(
                URL_PORT + "categoriesSortedByOrderAmount", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        List<Map<Object, Object>> body = categoryResponse.getBody();

        Integer firstElement = body.stream().map(item -> (Integer) item.get("total")).findFirst().get();
        Integer maxElement = body.stream().map(item -> (Integer) item.get("total")).max(Integer::compareTo).get();

        assertThat(categoryResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body).isNotNull().isExactlyInstanceOf(ArrayList.class);
        assertThat(body.size()).isNotZero();
        assertThat(maxElement).isEqualTo(firstElement);
    }

}
