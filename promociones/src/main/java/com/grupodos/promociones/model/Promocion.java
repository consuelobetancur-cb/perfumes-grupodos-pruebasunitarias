package com.grupodos.promociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "promociones")
@Data
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private Double porcentajeDescuento;

    @Column(nullable = false)
    private LocalDate fechaExpiracion;

    @Column(nullable = false)
    private Boolean activo;
}