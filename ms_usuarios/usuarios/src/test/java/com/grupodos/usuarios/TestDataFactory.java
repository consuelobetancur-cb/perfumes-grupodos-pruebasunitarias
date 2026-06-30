package com.grupodos.usuarios;

import com.grupodos.usuarios.dto.UsuarioRequestDTO;
import com.grupodos.usuarios.model.Usuario;
import net.datafaker.Faker;

public class TestDataFactory {

    private static final Faker faker = new Faker();

    public static Usuario unUsuario() {
        Usuario u = new Usuario();
        u.setId(faker.number().numberBetween(1L, 999L));
        u.setNombre(faker.name().fullName());
        u.setEmail(faker.internet().emailAddress());
        u.setTelefono(faker.phoneNumber().cellPhone());
        u.setDireccionEnvio(faker.address().fullAddress());
        u.setActivo(true);
        return u;
    }

    public static UsuarioRequestDTO unUsuarioRequest() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre(faker.name().fullName());
        dto.setEmail(faker.internet().emailAddress());
        dto.setTelefono(faker.phoneNumber().cellPhone());
        dto.setDireccionEnvio(faker.address().fullAddress());
        return dto;
    }
}