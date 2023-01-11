package com.example.system.controller;

import com.example.system.model.Order;
import com.example.system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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
}
