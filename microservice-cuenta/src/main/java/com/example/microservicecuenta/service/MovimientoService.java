package com.example.microservicecuenta.service;

import com.example.microservicecuenta.domain.Cuenta;
import com.example.microservicecuenta.domain.Movimiento;
import com.example.microservicecuenta.dto.CuentaReporteDTO;
import com.example.microservicecuenta.dto.MovimientoDTO;
import com.example.microservicecuenta.dto.ReporteEstadoCuentaDTO;
import com.example.microservicecuenta.exception.SaldoInsuficienteException;
import com.example.microservicecuenta.repository.CuentaRepository;
import com.example.microservicecuenta.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Transactional
    public Movimiento registrarMovimiento(Movimiento movimiento) {
        Cuenta cuentaInput = movimiento.getCuenta();
        if (cuentaInput == null || cuentaInput.getId() == null) {
            throw new IllegalArgumentException("La cuenta es requerida para el movimiento");
        }

        Cuenta cuenta = cuentaRepository.findById(cuentaInput.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        double saldoActual = cuenta.getSaldoInicial() + movimientoRepository.findAll().stream()
                .filter(m -> m.getCuenta().getId().equals(cuenta.getId()))
                .mapToDouble(Movimiento::getValor)
                .sum();

        double nuevoSaldo = saldoActual + movimiento.getValor();

        if (nuevoSaldo < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible");
        }

        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        return movimientoRepository.save(movimiento);
    }

    public ReporteEstadoCuentaDTO generarReporteEstadoCuenta(String clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // Buscar todas las cuentas del cliente
        List<Cuenta> cuentas = cuentaRepository.findAll().stream()
                .filter(c -> c.getClienteId().equals(clienteId))
                .collect(Collectors.toList());

        // Crear el DTO del reporte
        ReporteEstadoCuentaDTO reporte = new ReporteEstadoCuentaDTO();
        reporte.setCliente(clienteId);

        // Mapear cuentas y movimientos
        List<CuentaReporteDTO> cuentasDTO = cuentas.stream().map(cuenta -> {
            CuentaReporteDTO cuentaDTO = new CuentaReporteDTO();
            cuentaDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
            cuentaDTO.setTipoCuenta(cuenta.getTipoCuenta());

            // Calcular saldo actual
            double saldo = cuenta.getSaldoInicial() + movimientoRepository.findAll().stream()
                    .filter(m -> m.getCuenta().getId().equals(cuenta.getId()))
                    .mapToDouble(Movimiento::getValor)
                    .sum();
            cuentaDTO.setSaldo(saldo);

            // Obtener movimientos en el rango de fechas
            List<Movimiento> movimientos = movimientoRepository.findByCuentaAndFechaBetween(cuenta, fechaInicio, fechaFin);
            List<MovimientoDTO> movimientosDTO = movimientos.stream().map(mov -> {
                MovimientoDTO movDTO = new MovimientoDTO();
                movDTO.setFecha(mov.getFecha());
                movDTO.setTipoMovimiento(mov.getTipoMovimiento());
                movDTO.setValor(mov.getValor());
                movDTO.setSaldo(mov.getSaldo());
                return movDTO;
            }).collect(Collectors.toList());
            cuentaDTO.setMovimientos(movimientosDTO);

            return cuentaDTO;
        }).collect(Collectors.toList());

        reporte.setCuentas(cuentasDTO);
        return reporte;
    }
}