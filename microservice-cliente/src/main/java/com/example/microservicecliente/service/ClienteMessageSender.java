package com.example.microservicecliente.service;

import com.example.microservicecliente.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendClienteCreatedMessage(String clienteId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, clienteId);
        System.out.println("Mensaje enviado: Cliente creado con ID " + clienteId);
    }
}