package com.example.system.repository;

import com.example.system.model.Order;

import java.util.List;

public interface OrderRepository {

    List<Order> getOrders();

    Order createOrder(Integer id, Order order);

    Order getOrder(Integer id);

    Integer deleteOrder(Integer id);

    List<Order> getValidSortedOrdersByUserId(Integer id);

    Integer getSumOfAllOrdersByUserId(Integer id);
}
