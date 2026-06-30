package com.grupodos.carrito.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "carrito")
@EqualsAndHashCode(exclude = "carrito")
@Entity
@Table(name = "items_carrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long perfumeId;

    @Column(nullable = false)
    private String nombrePerfume;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    
    public ItemCarrito(Long perfumeId, String nombrePerfume, BigDecimal precioUnitario, Integer cantidad, Carrito carrito) {
        this.perfumeId = perfumeId;
        this.nombrePerfume = nombrePerfume;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.carrito = carrito;
    }


}
