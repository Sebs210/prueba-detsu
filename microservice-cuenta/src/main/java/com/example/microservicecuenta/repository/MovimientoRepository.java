package com.example.microservicecuenta.repository;

import com.example.microservicecuenta.domain.Cuenta;
import com.example.microservicecuenta.domain.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    // Método personalizado para buscar movimientos por cuenta y rango de fechas
    List<Movimiento> findByCuentaAndFechaBetween(Cuenta cuenta, LocalDateTime start, LocalDateTime end);
}