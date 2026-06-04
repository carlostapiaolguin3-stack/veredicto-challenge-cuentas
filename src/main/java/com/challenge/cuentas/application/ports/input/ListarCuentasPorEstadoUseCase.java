package com.challenge.cuentas.application.ports.input;

import java.util.List;

import com.challenge.cuentas.domain.model.Cuenta;

public interface ListarCuentasPorEstadoUseCase {
    List<Cuenta> listarPorEstado(Cuenta.Estado estado);
}
