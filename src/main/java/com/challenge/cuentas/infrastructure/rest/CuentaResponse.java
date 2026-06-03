package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.domain.model.Cuenta;

import java.math.BigDecimal;

/**
 * DTO de respuesta web. Aísla el modelo de dominio de lo que se expone por HTTP.
 */
public record CuentaResponse(
        String numero,
        String titular,
        BigDecimal saldoTotal,
        BigDecimal lineaCredito,
        String estado
) {
    public static CuentaResponse from(Cuenta c) {
        return new CuentaResponse(
                c.numero().valor(), c.titular(), c.saldoTotal(), c.lineaCredito(), c.estado().name());
    }
}
