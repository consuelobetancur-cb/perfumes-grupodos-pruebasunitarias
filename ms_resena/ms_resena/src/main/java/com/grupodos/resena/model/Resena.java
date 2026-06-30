package com.grupodos.resena.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @NotNull(message = "El ID del perfume es obligatorio")
    @Column(name = "perfume_id", nullable = false)

    private Long perfumeId;
    @NotBlank(message = "El nombre del perfume es obligatorio")
    @Column(name = "nombre_perfume", nullable = false, length = 200)

    private String nombrePerfume;
    @NotBlank(message = "El usuario es obligatorio")
    @Column(nullable = false, length = 100)

    private String usuario;
    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column(nullable = false)

    private Integer calificacion;
    @NotBlank(message = "El comentario no puede estar vacío")
    @Column(length = 1000)

    private String comentario;
    @Column(name = "fecha_resena", nullable = false)
    
    private LocalDateTime fechaResena = LocalDateTime.now();
}