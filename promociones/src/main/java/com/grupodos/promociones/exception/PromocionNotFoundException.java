package com.grupodos.promociones.exception;

public class PromocionNotFoundException extends RuntimeException {
    public PromocionNotFoundException(String mensaje) {
        super(mensaje);
    }
}