package com.grupodos.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventarios")
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    @NotNull(message = "El ID del perfume es obligatorio")
    @Column(name = "perfume_id", nullable = false)
    private Long perfumeId;

    @NotBlank(message = "La sucursal es obligatoria")
    @Column(nullable = false, length = 100)

    private String sucursal;
    @NotNull(message = "La cantidad en stock es obligatoria")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)

    private Integer cantidad;
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
}