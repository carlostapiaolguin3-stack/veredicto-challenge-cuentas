package com.challenge.cuentas.application.ports.input;

import java.util.List;

import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;

public interface ListarCuentaActivaUseCase {
    List<Cuenta> esCuentaActiva(NumeroCuenta numero);

}
