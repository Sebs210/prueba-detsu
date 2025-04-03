package com.example.microservicecliente.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cliente extends Persona {
    @Column(unique = true, nullable = false)
    private String clienteId;

    @Column(nullable = false)
    private String contrasena;

    private boolean estado;
}