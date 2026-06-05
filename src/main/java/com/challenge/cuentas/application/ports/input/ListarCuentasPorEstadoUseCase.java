package com.challenge.cuentas.application.ports.input;

import com.challenge.cuentas.domain.model.Cuenta;

import java.util.List;

public interface ListarCuentasPorEstadoUseCase {
    List<Cuenta> listar(Cuenta.Estado estado);
}
