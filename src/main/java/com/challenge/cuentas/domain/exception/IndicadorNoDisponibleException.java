package com.challenge.cuentas.domain.exception;

public class IndicadorNoDisponibleException extends RuntimeException {

    public IndicadorNoDisponibleException() {
        super("El indicador no está disponible");
    }

}
