package com.example.microservicecuenta.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cuenta")
@Data
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroCuenta;

    @Column(nullable = false)
    private String tipoCuenta;

    private double saldoInicial;

    private boolean estado;

    @Column(nullable = false)
    private String clienteId; // Referencia al cliente
}