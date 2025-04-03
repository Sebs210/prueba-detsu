package com.example.microservicecuenta.dto;

import lombok.Data;

import java.util.List;

@Data
public class CuentaReporteDTO {
    private String numeroCuenta;
    private String tipoCuenta;
    private double saldo;
    private List<MovimientoDTO> movimientos;
}