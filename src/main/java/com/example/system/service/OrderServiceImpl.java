package com.example.system.service;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Order;
import com.example.system.model.UserOrder;
import com.example.system.repository.OrderJpaRepository;
import com.example.system.repository.OrderRepository;
import com.example.system.repository.UserOrderJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private UserOrderJpaRepository userOrderJpaRepository;

    @Override
    public List<Order> getOrders() {
        return orderJpaRepository.getOrders(Sort.by("id"));
    }

    @Override
    public Order getOrder(Integer id) {
        Order order = orderJpaRepository.getOrder(id);
        if (null == order) {
            throw new ResourceNotFoundException("An order with id " + id + " does not exist");
        } else {
            return order;
        }
    }

    @Override
    public Order createOrder(Integer id, Order order) {
        return orderRepository.createOrder(id, order);
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
    public List<UserOrder> getUsersWithOrders() {
        return userOrderJpaRepository.getUsersWithOrders();
    }

    @Override
    public List<UserOrder> getUsersWithOrdersWithStatus(String status) {
        return orderRepository.getUsersWithOrdersWithStatus(status);
    }

}
