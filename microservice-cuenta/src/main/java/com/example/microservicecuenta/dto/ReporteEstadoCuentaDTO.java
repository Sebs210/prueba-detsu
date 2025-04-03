package com.example.microservicecuenta.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReporteEstadoCuentaDTO {
    private String cliente;
    private List<CuentaReporteDTO> cuentas;
}