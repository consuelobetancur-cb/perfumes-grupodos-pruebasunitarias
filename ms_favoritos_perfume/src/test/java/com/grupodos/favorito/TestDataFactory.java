package com.grupodos.favorito;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.grupodos.favorito.dto.FavoritoRequestDTO;
import com.grupodos.favorito.dto.PerfumeDTO;
import com.grupodos.favorito.dto.UsuarioDTO;
import com.grupodos.favorito.model.Favorito;

import net.datafaker.Faker;

public class TestDataFactory {
    private static final Faker faker = new Faker();

    public static PerfumeDTO unPerfumeDTO() {
    PerfumeDTO dto = new PerfumeDTO();
    
    dto.setId(faker.number().numberBetween(1L, 999L));
    dto.setNombre(faker.commerce().productName()); // Nombre genérico de producto
    dto.setMarca(faker.company().name());          // Usamos empresa como marca
    dto.setPrecio(BigDecimal.valueOf(faker.number().randomDouble(2, 10000, 150000))
            .setScale(2, RoundingMode.HALF_UP));
    dto.setStock(faker.number().numberBetween(0, 100)); // Cantidad de stock realista
    dto.setCategoriaNombre(faker.commerce().department()); // Categoría del perfume
    
    return dto;
}
    public static UsuarioDTO unUsuarioDTO() {
    UsuarioDTO dto = new UsuarioDTO();
    
    dto.setId(faker.number().numberBetween(1L, 999L));
    dto.setNombre(faker.name().fullName()); // Nombre completo es más realista que username
    dto.setEmail(faker.internet().emailAddress());
    dto.setActivo(true); // creamos usuarios activos para las pruebas
    
    return dto;
}
    public static FavoritoRequestDTO unFavoritoRequest(Long perfumeId, String usuarioNombre) {
        FavoritoRequestDTO request = new FavoritoRequestDTO();
        request.setPerfumeId(perfumeId);
        request.setUsuario(usuarioNombre);
        return request;
    }

    public static Favorito unFavorito(Long perfumeId, String nombrePerfume, String usuario) {
        Favorito favorito = new Favorito();
        favorito.setPerfumeId(perfumeId);
        favorito.setNombrePerfume(nombrePerfume);
        favorito.setUsuario(usuario);
        return favorito;
    }

}
