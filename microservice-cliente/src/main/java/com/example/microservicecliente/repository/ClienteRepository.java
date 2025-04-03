package com.example.microservicecliente.repository;

import com.example.microservicecliente.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Método personalizado para buscar por clienteId
    Cliente findByClienteId(String clienteId);
}