package com.example.microservicecuenta.controller;

import com.example.microservicecuenta.domain.Cuenta;
import com.example.microservicecuenta.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<Cuenta> createCuenta(@RequestBody Cuenta cuenta) {
        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        return ResponseEntity.ok(savedCuenta);
    }

    // READ (todos)
    @GetMapping
    public ResponseEntity<List<Cuenta>> getAllCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        return ResponseEntity.ok(cuentas);
    }

    // READ (por ID)
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> getCuentaById(@PathVariable Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        return cuenta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> updateCuenta(@PathVariable Long id, @RequestBody Cuenta cuentaDetails) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        if (cuenta.isPresent()) {
            Cuenta existingCuenta = cuenta.get();
            existingCuenta.setNumeroCuenta(cuentaDetails.getNumeroCuenta());
            existingCuenta.setTipoCuenta(cuentaDetails.getTipoCuenta());
            existingCuenta.setSaldoInicial(cuentaDetails.getSaldoInicial());
            existingCuenta.setEstado(cuentaDetails.isEstado());
            existingCuenta.setClienteId(cuentaDetails.getClienteId());
            Cuenta updatedCuenta = cuentaRepository.save(existingCuenta);
            return ResponseEntity.ok(updatedCuenta);
        }
        return ResponseEntity.notFound().build();
    }
}