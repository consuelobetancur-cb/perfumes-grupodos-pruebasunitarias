package com.grupodos.carrito;

import com.grupodos.carrito.dto.ItemCarritoRequestDTO;
import com.grupodos.carrito.dto.PerfumeDTO;
import com.grupodos.carrito.model.Carrito;
import com.grupodos.carrito.model.ItemCarrito;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestDataFactory {

    private static final Faker faker = new Faker();

    public static PerfumeDTO unPerfumeDTO() {
        PerfumeDTO dto = new PerfumeDTO();
        dto.setId(faker.number().numberBetween(1L, 999L));
        dto.setNombre(faker.commerce().productName());
        dto.setMarca(faker.company().name());
        dto.setPrecio(precio());
        dto.setCategoriaNombre(faker.commerce().department());
        return dto;
    }

    public static Carrito unCarrito() {
        return new Carrito(faker.name().username());
    }

    public static ItemCarrito unItem(PerfumeDTO perfume, Carrito carrito) {
        return new ItemCarrito(
                perfume.getId(),
                perfume.getNombre(),
                perfume.getPrecio(),
                faker.number().numberBetween(1, 5),
                carrito
        );
    }

    public static ItemCarritoRequestDTO unItemRequest(Long perfumeId) {
        ItemCarritoRequestDTO dto = new ItemCarritoRequestDTO();
        dto.setPerfumeId(perfumeId);
        dto.setCantidad(faker.number().numberBetween(1, 5));
        return dto;
    }

    private static BigDecimal precio() {
        double valor = faker.number().randomDouble(2, 10000, 50000);
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }
}