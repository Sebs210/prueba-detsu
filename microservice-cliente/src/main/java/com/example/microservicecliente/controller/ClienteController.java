package com.example.microservicecliente.controller;

import com.example.microservicecliente.domain.Cliente;
import com.example.microservicecliente.repository.ClienteRepository;
import com.example.microservicecliente.service.ClienteMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMessageSender clienteMessageSender;

    // CREATE
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        Cliente savedCliente = clienteRepository.save(cliente);
        clienteMessageSender.sendClienteCreatedMessage(cliente.getClienteId());
        return ResponseEntity.ok(savedCliente);
    }

    // READ (todos)
    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    // READ (por ID)
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            Cliente existingCliente = cliente.get();
            existingCliente.setNombre(clienteDetails.getNombre());
            existingCliente.setGenero(clienteDetails.getGenero());
            existingCliente.setEdad(clienteDetails.getEdad());
            existingCliente.setIdentificacion(clienteDetails.getIdentificacion());
            existingCliente.setDireccion(clienteDetails.getDireccion());
            existingCliente.setTelefono(clienteDetails.getTelefono());
            existingCliente.setClienteId(clienteDetails.getClienteId());
            existingCliente.setContrasena(clienteDetails.getContrasena());
            existingCliente.setEstado(clienteDetails.isEstado());
            Cliente updatedCliente = clienteRepository.save(existingCliente);
            return ResponseEntity.ok(updatedCliente);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            clienteRepository.delete(cliente.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}