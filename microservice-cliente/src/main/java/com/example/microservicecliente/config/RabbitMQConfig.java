package com.example.microservicecliente.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "cliente.exchange";
    public static final String QUEUE_NAME = "cliente.created.queue";
    public static final String ROUTING_KEY = "cliente.created";

    @Bean
    public TopicExchange clienteExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue clienteCreatedQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding binding(Queue clienteCreatedQueue, TopicExchange clienteExchange) {
        return BindingBuilder.bind(clienteCreatedQueue).to(clienteExchange).with(ROUTING_KEY);
    }
}