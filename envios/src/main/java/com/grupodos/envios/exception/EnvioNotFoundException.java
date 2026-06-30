package com.grupodos.envios.exception;

public class EnvioNotFoundException extends RuntimeException {
    public EnvioNotFoundException(String mensaje) {
        super(mensaje);
    }
}