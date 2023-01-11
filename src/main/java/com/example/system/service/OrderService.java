package com.example.system.service;

import com.example.system.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();

    Order createOrder(Integer id, Order order);

    Order getOrder(Integer id);

    Order deleteOrder(Integer id);
}
