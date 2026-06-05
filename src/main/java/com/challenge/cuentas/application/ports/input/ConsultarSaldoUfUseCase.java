package com.challenge.cuentas.application.ports.input;

import com.challenge.cuentas.domain.model.NumeroCuenta;
import com.challenge.cuentas.domain.model.SaldoUf;

public interface ConsultarSaldoUfUseCase {
    SaldoUf consultarSaldoUf(NumeroCuenta numero);

}

