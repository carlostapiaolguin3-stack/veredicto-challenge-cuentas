package com.challenge.cuentas.application.ports.output;

import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository {
    Optional<Cuenta> buscarPor(NumeroCuenta numero);

    List<Cuenta> listarPorEstado(Cuenta.Estado estado);
}
