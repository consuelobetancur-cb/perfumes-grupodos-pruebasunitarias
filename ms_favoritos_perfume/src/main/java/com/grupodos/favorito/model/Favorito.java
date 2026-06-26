package com.grupodos.favorito.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "favoritos")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long perfumeId;

    @Column(nullable = false)
    private String nombrePerfume;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private LocalDateTime fechaAgregado= LocalDateTime.now();;
}
