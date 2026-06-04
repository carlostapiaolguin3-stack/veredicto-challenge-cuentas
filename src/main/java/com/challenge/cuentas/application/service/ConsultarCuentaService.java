package com.challenge.cuentas.application.service;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.input.ListarCuentasPorEstadoUseCase;
import com.challenge.cuentas.application.ports.output.CuentaRepository;
import com.challenge.cuentas.domain.exception.CuentaNotFoundException;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Caso de uso. Orquesta los puertos para cumplir el puerto de entrada.
 * Sin HTTP, sin DTOs, sin BD: solo dominio y puertos. Se inyecta el puerto
 * de salida (no la implementación concreta).
 */
@Service
public class ConsultarCuentaService implements ConsultarCuentaUseCase,ListarCuentasPorEstadoUseCase {

    private final CuentaRepository repositorio;

    public ConsultarCuentaService(CuentaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Cuenta consultar(NumeroCuenta numero) {
        return repositorio.buscarPor(numero)
                .orElseThrow(() -> new CuentaNotFoundException(numero));
    }

    @Override
    public List<Cuenta> listarPorEstado(Cuenta.Estado estado) {
        return repositorio.buscarPorEstado(estado);
    }
}
