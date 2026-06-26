package com.grupodos.pedidoperfume;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.grupodos.pedidoperfume.dto.PedidoRequestDTO;
import com.grupodos.pedidoperfume.dto.PerfumeDTO;
import com.grupodos.pedidoperfume.model.Pedido;

import net.datafaker.Faker;

public class TestDataFactory {
    private static final Faker faker = new Faker();

    public static PerfumeDTO unPerfumeDTO() {
        PerfumeDTO dto = new PerfumeDTO();
        dto.setId(faker.number().numberBetween(1L, 999L));
        dto.setNombre(faker.commerce().productName());
        dto.setMarca(faker.company().name());
        dto.setPrecio(precio());
        dto.setStock(faker.number().numberBetween(1, 100));
        dto.setCategoriaNombre(faker.commerce().department());
        return dto;
    }

    public static PedidoRequestDTO unPedidoRequest(Long perfumeId) {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setPerfumeId(perfumeId);
        dto.setCliente(faker.name().username());
        dto.setCantidad(faker.number().numberBetween(1, 5));
        dto.setOpcionEnvio("Express");
        dto.setCuponDescuento("DESC2026");
        return dto;
    }

    public static Pedido unPedido(PerfumeDTO perfume, String cliente, Integer cantidad) {
        Pedido p = new Pedido();
        p.setId(faker.number().numberBetween(1L, 999L));
        p.setPerfumeId(perfume.getId());
        p.setNombrePerfume(perfume.getNombre());
        p.setCliente(cliente);
        p.setCantidad(cantidad);
        p.setFechaPedido(LocalDateTime.now());
        p.setEstadoPedido("PAGO_PENDIENTE");
        // Calculamos el total aquí basándonos en el precio del perfume
        p.setTotal(perfume.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
        p.setOpcionEnvio("Express");
        return p;
    }

    private static BigDecimal precio() {
        return BigDecimal.valueOf(faker.number().randomDouble(2, 5000, 80000))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
