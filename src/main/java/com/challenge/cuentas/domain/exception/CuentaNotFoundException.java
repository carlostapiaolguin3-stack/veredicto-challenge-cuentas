package com.challenge.cuentas.domain.exception;

import com.challenge.cuentas.domain.model.NumeroCuenta;

public class CuentaNotFoundException extends RuntimeException {
    public CuentaNotFoundException(NumeroCuenta numero) {
        super("cuenta no encontrada: " + numero);
    }
}
