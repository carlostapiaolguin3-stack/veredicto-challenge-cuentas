package com.challenge.cuentas.infrastructure.rest;

import java.math.BigDecimal;

public record SaldoResponse(String numero, BigDecimal saldoDisponible) {
}
