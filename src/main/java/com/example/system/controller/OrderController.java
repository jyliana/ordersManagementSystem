package com.example.system.controller;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Order;
import com.example.system.model.User;
import com.example.system.model.dto.FullOrder;
import com.example.system.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/order/{id}")
    public Order getOrderById(@PathVariable Integer id) {
        return orderService.getOrder(id);
    }

    @PostMapping("/createOrder/user/{id}")
    public Order createOrder(@PathVariable Integer id, @RequestBody FullOrder order) {
        if (null == order.getProducts()) {
            throw new ResourceNotFoundException("Order must have at least one product.");
        }
        return orderService.createOrder(id, order);
    }

    @PostMapping("/deleteOrder/{id}")
    public Order deleteOrder(@PathVariable Integer id) {
        return orderService.deleteOrder(id);
    }

    // All valid user orders sorted by trade date
    @GetMapping("/validSortedOrders/user/{id}")
    public List<Order> getValidSortedOrdersByUserId(@PathVariable Integer id) {
        return orderService.getValidSortedOrdersByUserId(id);
    }

    // All users with orders sorted by trade date
    @GetMapping("/usersWithOrders")
    public Map<User, List<FullOrder>> getUsersWithOrders() {
        return orderService.getUsersWithOrders();
    }

    // All users with orders with the specific status
    @GetMapping("/usersWithOrdersWithStatus/{status}")
    public Map<User, List<FullOrder>> getUsersWithOrdersWithStatus(@PathVariable String status) {
        return orderService.getUsersWithOrdersWithStatus(status.toUpperCase());
    }

    // Sum of all user’s orders
    @GetMapping("/sumOfAllOrders/user/{id}")
    public Integer getSumOfAllOrdersByUserId(@PathVariable Integer id) {
        return orderService.getSumOfAllOrdersByUserId(id);
    }

    // Users with the orders that have products from the defined category
    @GetMapping("/usersWithOrdersWithProductsFromCategory/{category}")
    public Map<User, List<FullOrder>> getUsersWithOrdersWithProductsFromCategory(@PathVariable String category) {
        return orderService.getUsersWithOrdersWithProductsFromCategory(category);
    }
}
