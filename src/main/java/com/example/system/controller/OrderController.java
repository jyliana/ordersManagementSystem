package com.example.system.controller;

import com.example.system.model.Order;
import com.example.system.model.User;
import com.example.system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/orders")
    public @ResponseBody List<Order> getAllOrders() {
        return orderService.getOrders();
    }


    @PostMapping("/createOrder/user/{id}")
    public @ResponseBody Order createOrder(@PathVariable Integer id, @RequestBody Order order) {
        return orderService.createOrder(id, order);
    }

    @GetMapping("/order/{id}")
    public @ResponseBody Order getOrderById(@PathVariable Integer id) {
        return orderService.getOrder(id);
    }

    @PostMapping("/deleteOrder/{id}")
    public @ResponseBody Order deleteOrder(@PathVariable Integer id) {
        return orderService.deleteOrder(id);
    }

    // All valid user orders sorted by trade date
    @GetMapping("/validSortedOrders/user/{id}")
    public @ResponseBody List<Order> getValidSortedOrdersByUserId(@PathVariable Integer id) {
        return orderService.getValidSortedOrdersByUserId(id);
    }

    // All users with orders sorted by trade date
    @GetMapping("/usersWithOrders")
    public @ResponseBody Map<User, List<Order>> getUsersWithOrders() {
        return orderService.getUsersWithOrders();
    }

    // All users with orders with the specific status
    @GetMapping("/usersWithOrdersWithStatus/{status}")
    public @ResponseBody Map<User, List<Order>> getUsersWithOrdersWithStatus(@PathVariable String status) {
        return orderService.getUsersWithOrdersWithStatus(status);
    }

    // Sum of all user’s orders
    @GetMapping("/sumOfAllOrders/user/{id}")
    public ResponseEntity<String> getSumOfAllOrdersByUserId(@PathVariable Integer id) {
        Integer sum = orderService.getSumOfAllOrdersByUserId(id);
        return ResponseEntity.ok(String.format("Sum of all orders for the user id %s: %d", id, sum));
    }
}
