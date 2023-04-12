package com.example.system.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqSchemaConfig {

    @Value("${rabbitmq.queue.customer.name}")
    private String customerQueue;

    @Value("${rabbitmq.queue.manager.name}")
    private String managerQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Bean
    public Declarables rabbitmqSchema() {
        return new Declarables(
                new DirectExchange(exchange, true, false),
                new Queue(customerQueue),
                new Binding(customerQueue, Binding.DestinationType.QUEUE, exchange, "BOOKED", null),
                new Binding(customerQueue, Binding.DestinationType.QUEUE, exchange, "UNPAID", null),
                new Queue(managerQueue),
                new Binding(managerQueue, Binding.DestinationType.QUEUE, exchange, "VALID", null)
        );
    }
}
