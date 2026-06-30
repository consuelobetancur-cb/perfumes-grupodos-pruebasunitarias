package com.grupodos.resena;

import com.grupodos.resena.dto.ResenaRequestDTO;
import com.grupodos.resena.model.Resena;
import net.datafaker.Faker;

public class TestDataFactory {
    private static final Faker faker = new Faker();

    public static Resena unaResena() {
    Resena r = new Resena();
    r.setId(faker.number().numberBetween(1L, 999L));
    r.setPerfumeId(faker.number().numberBetween(1L, 50L));
    r.setNombrePerfume("Perfume Test");
    r.setUsuario("usuario_test");
    r.setCalificacion(5);
    r.setComentario("Excelente producto");
    return r;
}
    public static ResenaRequestDTO unResenaRequest() {
        ResenaRequestDTO dto = new ResenaRequestDTO();
        dto.setPerfumeId(1L);
        dto.setNombrePerfume("Perfume Test");
        dto.setUsuario("usuario_test");
        dto.setCalificacion(5);
        dto.setComentario("Excelente producto");
        return dto;
    }
}