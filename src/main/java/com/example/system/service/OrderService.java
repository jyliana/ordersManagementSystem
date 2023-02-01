package com.example.system.service;

import com.example.system.model.Order;
import com.example.system.model.User;
import com.example.system.model.dto.FullOrder;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> getOrders();

    Order createOrder(Integer id, FullOrder order);

    Order getOrder(Integer id);

    Order deleteOrder(Integer id);

    List<Order> getValidSortedOrdersByUserId(Integer id);

    Integer getSumOfAllOrdersByUserId(Integer id);

    Map<User, List<FullOrder>> getUsersWithOrders();

    Map<User, List<FullOrder>> getUsersWithOrdersWithStatus(String status);

    Map<User, List<FullOrder>> getUsersWithOrdersWithProductsFromCategory(String category);
}
