package com.challenge.cuentas.application.ports.input;

import com.challenge.cuentas.domain.model.NumeroCuenta;

import java.math.BigDecimal;

public interface ConsultarSaldoUfUseCase {

    record Resultado(String numero, BigDecimal saldoDisponible, BigDecimal valorUf, BigDecimal saldoEnUf) {}

    Resultado consultar(NumeroCuenta numero);
}
