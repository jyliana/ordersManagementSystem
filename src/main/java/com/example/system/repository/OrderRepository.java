package com.example.system.repository;

import com.example.system.model.Order;
import com.example.system.model.UserOrder;

import java.util.List;

public interface OrderRepository {

    List<Order> getOrders();

    Order createOrder(Integer id, Order order);

    Order getOrder(Integer id);

    Order deleteOrder(Integer id);

    List<Order> getValidSortedOrdersByUserId(Integer id);

    Integer getSumOfAllOrdersByUserId(Integer id);

    List<UserOrder> getUsersWithOrdersWithStatus(String status);
}
