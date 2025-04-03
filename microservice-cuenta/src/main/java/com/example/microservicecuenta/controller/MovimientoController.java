package com.example.microservicecuenta.controller;

import com.example.microservicecuenta.domain.Movimiento;
import com.example.microservicecuenta.dto.ReporteEstadoCuentaDTO;
import com.example.microservicecuenta.repository.MovimientoRepository;
import com.example.microservicecuenta.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private MovimientoService movimientoService;

    // CREATE
    @PostMapping
    public ResponseEntity<Movimiento> createMovimiento(@RequestBody Movimiento movimiento) {
        Movimiento savedMovimiento = movimientoService.registrarMovimiento(movimiento);
        return ResponseEntity.ok(savedMovimiento);
    }

    // READ (todos)
    @GetMapping
    public ResponseEntity<List<Movimiento>> getAllMovimientos() {
        List<Movimiento> movimientos = movimientoRepository.findAll();
        return ResponseEntity.ok(movimientos);
    }

    // READ (por ID)
    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> getMovimientoById(@PathVariable Long id) {
        Optional<Movimiento> movimiento = movimientoRepository.findById(id);
        return movimiento.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Movimiento> updateMovimiento(@PathVariable Long id, @RequestBody Movimiento movimientoDetails) {
        Optional<Movimiento> movimiento = movimientoRepository.findById(id);
        if (movimiento.isPresent()) {
            Movimiento existingMovimiento = movimiento.get();
            existingMovimiento.setFecha(movimientoDetails.getFecha());
            existingMovimiento.setTipoMovimiento(movimientoDetails.getTipoMovimiento());
            existingMovimiento.setValor(movimientoDetails.getValor());
            existingMovimiento.setSaldo(movimientoDetails.getSaldo());
            existingMovimiento.setCuenta(movimientoDetails.getCuenta());
            Movimiento updatedMovimiento = movimientoRepository.save(existingMovimiento);
            return ResponseEntity.ok(updatedMovimiento);
        }
        return ResponseEntity.notFound().build();
    }

    // Reporte de estado de cuenta
    @GetMapping("/reportes")
    public ResponseEntity<ReporteEstadoCuentaDTO> getReporteEstadoCuenta(
            @RequestParam("fecha") String rangoFechas,
            @RequestParam("cliente") String clienteId) {
        try {
            // Dividir el rango de fechas en inicio y fin
            String[] fechas = rangoFechas.split("(?<=\\d{4}-\\d{2}-\\d{2})-(?=\\d{4}-\\d{2}-\\d{2})");
            if (fechas.length != 2) {
                return ResponseEntity.badRequest().body(null); // Formato inválido
            }

            // Parsear las fechas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime fechaInicio = LocalDateTime.parse(fechas[0] + "T00:00:00");
            LocalDateTime fechaFin = LocalDateTime.parse(fechas[1] + "T23:59:59");

            ReporteEstadoCuentaDTO reporte = movimientoService.generarReporteEstadoCuenta(clienteId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null); // Error en el formato de fecha
        }
    }
}