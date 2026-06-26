package com.grupodos.envios.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "envios")
@Data
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long pedidoId;
    private String region;
    private String direccion;
    private Double costoEnvio;
    private String estado;
}