package com.example.system.repository;

import com.example.system.model.Order;
import com.example.system.model.User;

import java.util.List;
import java.util.Map;

public interface OrderRepository {

    List<Order> getOrders();

    Order createOrder(Integer id, Order order);

    Order getOrder(Integer id);

    Order deleteOrder(Integer id);

    List<Order> getValidSortedOrdersByUserId(Integer id);

    Integer getSumOfAllOrdersByUserId(Integer id);

    Map<User, List<Order>> getUsersWithOrders();
}
