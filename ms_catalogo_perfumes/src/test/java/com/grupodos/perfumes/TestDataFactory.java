package com.grupodos.perfumes;

import net.datafaker.Faker;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.grupodos.perfumes.dto.PerfumeRequestDTO;
import com.grupodos.perfumes.model.Categoria;
import com.grupodos.perfumes.model.Perfume;

public class TestDataFactory {
    private static final Faker faker = new Faker();

    public static Categoria unaCategoria() {
        return new Categoria(
                faker.number().numberBetween(1L, 99L),
                faker.commerce().department(), // Genera un nombre de categoría (género)
                faker.lorem().sentence()      // Genera una descripción
        );
    }

    public static Perfume unPerfume(Categoria categoria) {
        return new Perfume(
                faker.number().numberBetween(1L, 999L),
                faker.commerce().productName(),      // Nombre del perfume
                faker.company().name(),              // Marca
                precio(),
                faker.number().numberBetween(1, 100), // Stock
                categoria
        );
    }

    public static PerfumeRequestDTO unPerfumeRequest(Long categoriaId) {
        PerfumeRequestDTO dto = new PerfumeRequestDTO();
        dto.setNombre(faker.commerce().productName());
        dto.setMarca(faker.company().name());
        dto.setPrecio(precio());
        dto.setStock(faker.number().numberBetween(1, 100));
        dto.setCategoriaId(categoriaId);
        return dto;
    }

    private static BigDecimal precio() {
        return BigDecimal.valueOf(faker.number().randomDouble(2, 10000, 200000))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static Perfume unPerfumeSinCategoria() {
    // definimos Perfume donde la categoría es null
    return new Perfume(1L, "Perfume Nulo", "Marca Test", new BigDecimal("50.00"), 10, null);
}
}
