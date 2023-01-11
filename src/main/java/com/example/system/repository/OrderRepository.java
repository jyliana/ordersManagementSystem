package com.example.system.repository;

import com.example.system.model.Order;

import java.util.List;

public interface OrderRepository {

    List<Order> getOrders();
}
