package com.challenge.cuentas.domain.exception;

public class ServicioExternoNoDisponibleException extends RuntimeException {
    public ServicioExternoNoDisponibleException(String servicio) {
        super("servicio externo no disponible: " + servicio);
    }
}
