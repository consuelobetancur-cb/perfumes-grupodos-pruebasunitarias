package com.grupodos.inventario;

import com.grupodos.inventario.dto.InventarioRequestDTO;
import com.grupodos.inventario.model.Inventario;
import net.datafaker.Faker;

import java.time.LocalDateTime;

public class TestDataFactory {
    private static final Faker faker = new Faker();
    public static Inventario unInventario() {
        Inventario i = new Inventario();
        i.setId(faker.number().numberBetween(1L, 999L));
        i.setPerfumeId(faker.number().numberBetween(1L, 100L));
        i.setSucursal(faker.address().city());
        i.setCantidad(faker.number().numberBetween(10, 500));
        i.setFechaActualizacion(LocalDateTime.now());
        return i;
    }

    public static InventarioRequestDTO unInventarioRequest() {
        InventarioRequestDTO dto = new InventarioRequestDTO();
        dto.setPerfumeId(faker.number().numberBetween(1L, 100L));
        dto.setSucursal(faker.address().city());
        dto.setCantidad(faker.number().numberBetween(10, 500));
        return dto;
    }
}