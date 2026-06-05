package com.challenge.cuentas.application.ports.input;

import java.util.List;

import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.Cuenta.Estado;
import com.challenge.cuentas.domain.model.NumeroCuenta;

/**
 * Puerto de entrada. Lo que la aplicación ofrece hacia afuera.
 */
public interface ConsultarCuentaUseCase {
    Cuenta consultar(NumeroCuenta numero);

    List<Cuenta> listarPorEstado(Estado estado);
}
