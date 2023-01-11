package com.example.system.controller;

import com.example.system.model.Order;
import com.example.system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public @ResponseBody
    List<Order> getAllOrders() {
        return orderService.getOrders();
    }
}
