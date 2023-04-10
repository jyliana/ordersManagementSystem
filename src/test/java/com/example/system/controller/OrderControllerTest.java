package com.example.system.controller;

import com.example.system.model.Order;
import com.example.system.model.dto.BookedProduct;
import com.example.system.model.dto.FullOrder;
import com.example.system.model.enums.Status;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(OrderAnnotation.class)
class OrderControllerTest {
    private static final String URL_PORT = "http://localhost:8080/";
    private static final Status BOOKED_STATUS = Status.BOOKED;
    private static final Status STATUS = Status.DELETED;
    private static final Integer QUANTITY = 60;
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
    void testGetAllOrders() {
        ResponseEntity<List<Order>> orderResponse = restTemplate.exchange(URL_PORT + "orders", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        assertListOfOrders(orderResponse);
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetUsersWithOrders() {
        ResponseEntity<Map<Object, List<Order>>> orderResponse = restTemplate.exchange(URL_PORT + "usersWithOrders", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });

        Collection<List<Order>> values = orderResponse.getBody().values();
        boolean resultOfAllStatuses = values.stream().allMatch(m -> m.stream().allMatch(order -> {
            Status status = order.getStatus();
            return EnumSet.allOf(Status.class).contains(status);
        }));

        assertSoftly(softly -> {
            softly.assertThat(values).isNotNull();
            softly.assertThat(resultOfAllStatuses).isTrue();
        });

    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetValidSortedOrdersByUserId() {
        ResponseEntity<List<Order>> orderResponse = restTemplate.exchange(URL_PORT + "validSortedOrders/user/1", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        assertListOfOrders(orderResponse);
        assertThat(
                orderResponse.getBody().stream().limit(3).allMatch(order -> order.getStatus().equals(Status.VALID)))
                .isTrue();
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetUsersWithOrdersWithStatus() {
        ResponseEntity<Map<Object, List<Order>>> orderResponse = restTemplate.exchange(
                URL_PORT + "usersWithOrdersWithStatus/" + STATUS.name(), HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });

        Collection<List<Order>> values = orderResponse.getBody().values();

        boolean resultOfAllStatuses = values.stream().allMatch(m -> m.stream().allMatch(order -> order.getStatus().equals(STATUS)));

        assertSoftly(softly -> {
            softly.assertThat(orderResponse.getBody().isEmpty()).isFalse();
            softly.assertThat(resultOfAllStatuses).isTrue();
        });
    }


    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetOrderById() {
        Order order = restTemplate.getForObject(URL_PORT + "order/1", Order.class);
        assertOrder(order);
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetSumOfAllOrdersForUser() {
        Integer sum = restTemplate.getForObject(URL_PORT + "sumOfAllOrders/user/1", Integer.class);
        assertThat(sum).isExactlyInstanceOf(Integer.class).isNotNull();
    }

    @Disabled("In order to not create new orders constantly")
    @Test
    @Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
    void testShouldThrowExceptionWhenQuantityNotAvailable() {
        Order order = createOrder(QUANTITY);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> createOrder(QUANTITY));

        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(exception.getRawStatusCode()).isEqualTo(404);
                    softly.assertThat(exception.getMessage().contains("The required products are not available.")).isTrue();
                }
        );

        assertThat(order.getId()).isNotNull();
        assertThat(order.getAmount()).isEqualTo(QUANTITY);
        assertThat(order.getStatus()).isEqualTo(BOOKED_STATUS);
    }

    @Disabled("In order to not create new orders constantly")
    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testCreateOrderForUserId() {
        Order order = createOrder(10);
        Integer createdOrderId = order.getId();

        assertThat(createdOrderId).isNotNull();
        assertThat(order.getStatus()).isEqualTo(BOOKED_STATUS);
    }


    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testDeleteOrder() {
        Order result = restTemplate.postForObject(URL_PORT + "deleteOrder/1", null, Order.class);

        assertThat(result).isNotNull().isExactlyInstanceOf(Order.class);
        assertThat(result.getStatus()).isEqualTo(Status.DELETED);
    }

    private Order createOrder(Integer quantity) {
        BookedProduct product = BookedProduct.builder()
                .id(1)
                .quantity(quantity)
                .build();

        FullOrder order = FullOrder.builder()
                .status(BOOKED_STATUS)
                .products(List.of(product))
                .build();

        return restTemplate.postForObject(URL_PORT + "createOrder/user/1", order, Order.class);
    }

    private static void assertListOfOrders(final ResponseEntity<List<Order>> orderResponse) {
        List<Order> orders = orderResponse.getBody();

        assertSoftly(softly -> {
                    softly.assertThat(orderResponse.getStatusCode().is2xxSuccessful()).isTrue();
                    softly.assertThat(orders).isNotNull();
                    softly.assertThat(orders).isNotEmpty();
                    softly.assertThat(orders.get(0)).isExactlyInstanceOf(Order.class);
                    softly.assertThat(orders.get(0).getStatus()).isNotNull().isExactlyInstanceOf(Status.class);
                }
        );
    }

    private static void assertOrder(Order order) {
        assertSoftly(softly -> {
                    softly.assertThat(order).isNotNull();
                    softly.assertThat(order).isExactlyInstanceOf(Order.class);
                    softly.assertThat(order.getId()).isExactlyInstanceOf(Integer.class);
                    softly.assertThat(order.getTradeDate()).isExactlyInstanceOf(Timestamp.class);
                    softly.assertThat(order.getAmount()).isExactlyInstanceOf(Integer.class);
                    softly.assertThat(order.getStatus()).isExactlyInstanceOf(Status.class);
                }
        );
    }
}
