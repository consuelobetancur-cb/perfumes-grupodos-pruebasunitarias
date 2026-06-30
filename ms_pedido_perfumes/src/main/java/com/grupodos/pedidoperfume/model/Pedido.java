package com.grupodos.pedidoperfume.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cliente;

    @Column(nullable = false)
    private Long perfumeId;

    private String nombrePerfume;
    
    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    @Column(nullable = false)
    private String estadoPedido; //el estado debe se pago pendiente, pagado o anulado 

    @Column(nullable = false, precision = 10 ,scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 50)
    private String opcionEnvio;

    //opcional poner un cupon de descuento
    private String cuponDescuento;


}
