package com.grupodos.pagos;

import com.grupodos.pagos.dto.PagoRequestDTO;
import com.grupodos.pagos.model.Pagos;

import net.datafaker.Faker;
import java.time.LocalDateTime;

public class TestDataFactory {
    private static final Faker faker = new Faker();

    public static Pagos unPago() {
        Pagos p = new Pagos();
        p.setId(faker.number().numberBetween(1L, 999L));
        p.setPedidoId(faker.number().numberBetween(1000L, 5000L));
        p.setMonto(faker.number().randomDouble(2, 10, 1000));
        p.setMetodoPago("CREDITO");
        p.setEstado("PENDIENTE");
        p.setFechaPago(LocalDateTime.now());
        return p;
    }

    public static PagoRequestDTO unPagoRequest() {
        PagoRequestDTO dto = new PagoRequestDTO();
        dto.setPedidoId(faker.number().numberBetween(1000L, 5000L));
        dto.setMonto(100.0);
        dto.setMetodoPago("TARJETA");
        return dto;
    }
}