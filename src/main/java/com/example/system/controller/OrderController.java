package com.example.system.controller;

import com.example.system.model.Order;
import com.example.system.model.User;
import com.example.system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
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
    public Order createOrder(@PathVariable Integer id, @RequestBody Order order) {
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
    public Map<User, List<Order>> getUsersWithOrders() {
        return orderService.getUsersWithOrders();
    }

    // All users with orders with the specific status
    @GetMapping("/usersWithOrdersWithStatus/{status}")
    public Map<User, List<Order>> getUsersWithOrdersWithStatus(@PathVariable String status) {
        return orderService.getUsersWithOrdersWithStatus(status);
    }

    // Sum of all user’s orders
    @GetMapping("/sumOfAllOrders/user/{id}")
    public String getSumOfAllOrdersByUserId(@PathVariable Integer id) {
        Integer sum = orderService.getSumOfAllOrdersByUserId(id);
        return String.format("Sum of all orders for the user with id %s: %d", id, sum);
    }
}
