package com.example.microservicecuenta.service;

import com.example.microservicecuenta.config.RabbitMQConfig;
import com.example.microservicecuenta.domain.Cuenta;
import com.example.microservicecuenta.repository.CuentaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteMessageListener {

    @Autowired
    private CuentaRepository cuentaRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleClienteCreated(String clienteId) {
        System.out.println("Mensaje recibido: Cliente creado con ID " + clienteId);
        // Crear una cuenta automáticamente para el cliente
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("AUTO-" + clienteId);
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(1000.0);
        cuenta.setEstado(true);
        cuenta.setClienteId(clienteId);
        cuentaRepository.save(cuenta);
        System.out.println("Cuenta creada automáticamente para cliente " + clienteId);
    }
}