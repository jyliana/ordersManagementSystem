package com.example.system.producer;

import com.example.system.model.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.example.system.service.constants.Constants.EXCHANGE;


@Slf4j
@Service
@AllArgsConstructor
public class InvoiceProducer {

    private RabbitTemplate rabbitTemplate;

    public void sendOrderCreated(Order order) {
        log.info(String.format("A new order has been created -> %s", order));
        rabbitTemplate.convertAndSend(EXCHANGE, order.getStatus().name(), order);
    }
}