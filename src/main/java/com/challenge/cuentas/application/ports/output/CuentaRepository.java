package com.challenge.cuentas.application.ports.output;

import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida. Lo que el caso de uso necesita del mundo externo.
 * Devuelve modelos de dominio (no DTOs, no filas de BD) y Optional (no null).
 */
public interface CuentaRepository {
    Optional<Cuenta> buscarPor(NumeroCuenta numero);
    List<Cuenta> buscarPorEstado(Cuenta.Estado estado);
}
