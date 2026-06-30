package com.grupodos.perfumes.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El genero no puede estar vacio")
    @Size(max = 100, message = "El genero no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String genero;


    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    @Column(length = 255)
    private String descripcion;

   

}
