package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.application.ports.input.ConsultarSaldoUfUseCase;

import java.math.BigDecimal;

public record SaldoUfResponse(
        String numero,
        BigDecimal saldoDisponible,
        BigDecimal uf,
        BigDecimal saldoEnUf
) {
    public static SaldoUfResponse from(ConsultarSaldoUfUseCase.Resultado r) {
        return new SaldoUfResponse(r.numero(), r.saldoDisponible(), r.valorUf(), r.saldoEnUf());
    }
}
