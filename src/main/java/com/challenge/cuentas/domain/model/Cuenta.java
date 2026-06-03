package com.challenge.cuentas.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo de dominio. Record puro: sin frameworks. Las reglas simples del negocio
 * (saldo disponible, si está activa) viven acá, no en el servicio.
 */
public record Cuenta(
        NumeroCuenta numero,
        String titular,
        BigDecimal saldoTotal,
        BigDecimal lineaCredito,
        Estado estado,
        LocalDate fechaApertura
) {

    public BigDecimal saldoDisponible() {
        return saldoTotal.add(lineaCredito);
    }

    public boolean estaActiva() {
        return estado == Estado.ACTIVA;
    }

    public enum Estado {
        ACTIVA, BLOQUEADA, CERRADA
    }
}
