package com.example.microservicecuenta.repository;

import com.example.microservicecuenta.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    // Método personalizado para buscar por número de cuenta
    Cuenta findByNumeroCuenta(String numeroCuenta);
}