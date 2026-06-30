package com.grupodos.promociones;

import com.grupodos.promociones.dto.PromocionRequestDTO;
import com.grupodos.promociones.model.Promocion;
import net.datafaker.Faker;

import java.time.LocalDate;

public class TestDataFactory {

    private static final Faker faker = new Faker();

    public static PromocionRequestDTO unaPromocionRequestVálida() {
        PromocionRequestDTO dto = new PromocionRequestDTO();
        dto.setCodigo(faker.text().text(5, 10).toUpperCase() + faker.number().digits(2));
        dto.setPorcentajeDescuento(faker.number().randomDouble(1, 10, 50)); // Descuento entre 10% y 50%
        dto.setFechaExpiracion(LocalDate.now().plusDays(faker.number().numberBetween(5, 30))); // Futura
        return dto;
    }

    public static Promocion unaPromocionActivaYVigente(String codigo, Double porcentaje) {
        Promocion promo = new Promocion();
        promo.setId(faker.number().randomNumber());
        promo.setCodigo(codigo.toUpperCase().trim());
        promo.setPorcentajeDescuento(porcentaje);
        promo.setFechaExpiracion(LocalDate.now().plusDays(10));
        promo.setActivo(true);
        return promo;
    }

    public static Promocion unaPromocionExpirada(String codigo) {
        Promocion promo = new Promocion();
        promo.setId(faker.number().randomNumber());
        promo.setCodigo(codigo.toUpperCase().trim());
        promo.setPorcentajeDescuento(15.0);
        promo.setFechaExpiracion(LocalDate.now().minusDays(5)); // Ya expiró
        promo.setActivo(true);
        return promo;
    }
}