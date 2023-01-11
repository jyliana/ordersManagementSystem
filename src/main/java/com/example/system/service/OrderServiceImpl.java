package com.example.system.service;

import com.example.system.model.Order;
import com.example.system.model.User;
import com.example.system.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    @Override
    public Order createOrder(Integer id, Order order) {
        return orderRepository.createOrder(id, order);
    }

    @Override
    public Order getOrder(Integer id) {
        return orderRepository.getOrder(id);
    }

    @Override
    public Order deleteOrder(Integer id) {
        return orderRepository.deleteOrder(id);
    }

    @Override
    public List<Order> getValidSortedOrdersByUserId(Integer id) {
        return orderRepository.getValidSortedOrdersByUserId(id);
    }

    @Override
    public Integer getSumOfAllOrdersByUserId(Integer id) {
        return orderRepository.getSumOfAllOrdersByUserId(id);
    }

    @Override
    public Map<User, List<Order>> getUsersWithOrders() {
        return orderRepository.getUsersWithOrders();
    }

}
