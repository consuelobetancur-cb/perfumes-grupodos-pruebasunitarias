package com.grupodos.pagos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagos")
@NoArgsConstructor
@AllArgsConstructor
public class Pagos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El ID del pedido es obligatorio")
    @Column(name = "pedido_id", nullable = false)

    private Long pedidoId; 
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Column(nullable = false)

    private Double monto;
    @NotBlank(message = "El método de pago es obligatorio")
    @Column(name = "metodo_pago", nullable = false, length = 50)

    private String metodoPago; 
    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE"; 
    
    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();
}