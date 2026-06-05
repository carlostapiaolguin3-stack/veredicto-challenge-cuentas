package com.challenge.cuentas.domain.model;

import java.math.BigDecimal;

public record SaldoUf(
    NumeroCuenta numeroCuenta,
    BigDecimal saldoDisponible,
    BigDecimal valorUf,
    BigDecimal saldoEnUf
) {
} 
