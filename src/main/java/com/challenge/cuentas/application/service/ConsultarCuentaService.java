package com.challenge.cuentas.application.service;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.input.ConsultarSaldoUfUseCase;
import com.challenge.cuentas.application.ports.input.ListarCuentasPorEstadoUseCase;
import com.challenge.cuentas.application.ports.output.CuentaRepository;
import com.challenge.cuentas.application.ports.output.IndicadorRepository;
import com.challenge.cuentas.domain.exception.CuentaNotFoundException;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import com.challenge.cuentas.domain.model.SaldoUf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Caso de uso. Orquesta los puertos para cumplir el puerto de entrada.
 * Sin HTTP, sin DTOs, sin BD: solo dominio y puertos. Se inyecta el puerto
 * de salida (no la implementación concreta).
 */
@Service
public class ConsultarCuentaService implements ConsultarCuentaUseCase,ListarCuentasPorEstadoUseCase, ConsultarSaldoUfUseCase {

    private final CuentaRepository repositorio;
    private final IndicadorRepository indicadorRepository;

    public ConsultarCuentaService(CuentaRepository repositorio, IndicadorRepository indicadorRepository) {
        this.repositorio = repositorio;
        this.indicadorRepository = indicadorRepository;
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

    @Override
    public SaldoUf consultarSaldoUf(NumeroCuenta numero) {
        Cuenta cuenta = repositorio.buscarPor(numero)
            .orElseThrow(() -> new CuentaNotFoundException(numero));
        BigDecimal saldoDisponible = cuenta.saldoDisponible();
        BigDecimal valorUf = indicadorRepository.obtenerValorUf();
        BigDecimal saldoEnUf = saldoDisponible.divide(valorUf,2,RoundingMode.HALF_UP);

        return new SaldoUf(
            cuenta.numero(),
            saldoDisponible,
            valorUf,
            saldoEnUf
        );
    }
}
