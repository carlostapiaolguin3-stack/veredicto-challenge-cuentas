package com.challenge.cuentas.infrastructure.rest;

import java.math.BigDecimal;

import com.challenge.cuentas.domain.model.SaldoUf;

public record SaldoUfResponse(
    String numeroCuenta,
    BigDecimal saldoDisponible,
    BigDecimal valorUf,
    BigDecimal saldoEnUf
) {

    public static SaldoUfResponse from(SaldoUf saldoUf) {
        return new SaldoUfResponse(
            saldoUf.numeroCuenta().valor(),
            saldoUf.saldoDisponible(),
            saldoUf.valorUf(),
            saldoUf.saldoEnUf()
        );
    }

}
