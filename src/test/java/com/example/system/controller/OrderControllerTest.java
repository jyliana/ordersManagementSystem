package com.example.system.controller;

import com.example.system.model.Order;
import com.example.system.model.UserOrder;
import com.example.system.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@TestMethodOrder(OrderAnnotation.class)
class OrderControllerTest {
    private static final String URL_PORT = "http://localhost:8080/";
    private RestTemplate restTemplate;
    private static Integer createdOrderId;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetAllOrders() {
        ResponseEntity<List<Order>> orderResponse = restTemplate.exchange(URL_PORT + "orders", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        assertListOfOrders(orderResponse);
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetUsersWithOrders() {
        ResponseEntity<List<UserOrder>> orderResponse = restTemplate.exchange(URL_PORT + "usersWithOrders", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });

        List<UserOrder> userOrders = orderResponse.getBody().subList(0, 1);
        assertSoftly(softly -> {
            UserOrder userOrder = userOrders.get(0);
            softly.assertThat(userOrders).isNotNull();
            softly.assertThat(userOrder).isExactlyInstanceOf(UserOrder.class);
            softly.assertThat(userOrder.getStatus()).isIn(Status.VALID, Status.DELETED);
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
        ResponseEntity<List<UserOrder>> orderResponse = restTemplate.exchange(URL_PORT + "usersWithOrdersWithStatus/deleted", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        List<UserOrder> userOrders = orderResponse.getBody();
        assertSoftly(softly -> {
            UserOrder userOrder = userOrders.get(0);
            softly.assertThat(userOrders).isNotNull();
            softly.assertThat(userOrder).isExactlyInstanceOf(UserOrder.class);
            softly.assertThat(userOrder.getStatus()).isEqualTo(Status.DELETED);
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
    @org.junit.jupiter.api.Order(1)
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testCreateOrderForUserId() {
        Order order = Order.builder()
                .amount(333)
                .status(Status.VALID)
                .tradeDate(Date.valueOf("2023-01-17"))
                .build();

        order = restTemplate.postForObject(URL_PORT + "createOrder/user/1", order, Order.class);
        createdOrderId = order.getId();
        assertThat(createdOrderId).isNotNull();
        assertThat(order.getStatus()).isEqualTo(Status.VALID);
    }

    @Disabled("Works after testCreateOrder which is also disabled")
    @Test
    @org.junit.jupiter.api.Order(2)
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testDeleteOrder() {
        Order result = restTemplate.postForObject(URL_PORT + "deleteOrder/" + createdOrderId, null, Order.class);

        assertThat(result).isNotNull().isExactlyInstanceOf(Order.class);
        assertThat(result.getStatus()).isEqualTo(Status.DELETED);
    }

    private static void assertListOfOrders(final ResponseEntity<List<Order>> orderResponse) {
        List<Order> orders = orderResponse.getBody();

        assertSoftly(softly -> {
                    softly.assertThat(orderResponse.getStatusCode().is2xxSuccessful());
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
                    softly.assertThat(order.getTradeDate()).isExactlyInstanceOf(Date.class);
                    softly.assertThat(order.getAmount()).isExactlyInstanceOf(Integer.class);
                    softly.assertThat(order.getStatus()).isExactlyInstanceOf(Status.class);
                }
        );
    }
}
