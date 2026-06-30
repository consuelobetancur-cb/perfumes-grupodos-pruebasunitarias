package com.grupodos.envios;

import com.grupodos.envios.model.Envio;
import net.datafaker.Faker;

import java.util.Locale;

public class TestDataFactory {

    
    private static final Faker faker = new Faker(new Locale("es"));

    public static Envio unEnvioMetropolitana() {
        Envio envio = new Envio();
        envio.setId(null);
        envio.setPedidoId(faker.number().randomDigitNotZero() * 100L);
        envio.setRegion("Metropolitana");
        envio.setDireccion(faker.address().streetAddress());
        envio.setCostoEnvio(null);
        envio.setEstado(null);
        return envio;
    }

    public static Envio unEnvioRegiones() {
        Envio envio = new Envio();
        envio.setId(null);
        envio.setPedidoId(faker.number().randomDigitNotZero() * 100L);
        
        envio.setRegion(faker.options().option("Valparaíso", "Biobío", "Araucanía", "Antofagasta"));
        envio.setDireccion(faker.address().streetAddress());
        envio.setCostoEnvio(null);
        envio.setEstado(null);
        return envio;
    }

    public static Envio unEnvioGuardado(Long id, String region, Double costo, String estado) {
        Envio envio = new Envio();
        envio.setId(id);
        envio.setPedidoId(faker.number().randomDigitNotZero() * 100L);
        envio.setRegion(region);
        envio.setDireccion(faker.address().streetAddress());
        envio.setCostoEnvio(costo);
        envio.setEstado(estado);
        return envio;
    }
}