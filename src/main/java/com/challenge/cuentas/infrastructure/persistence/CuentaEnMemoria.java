package com.challenge.cuentas.infrastructure.persistence;

import com.challenge.cuentas.application.ports.output.CuentaRepository;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adapter de salida: datos en memoria. Implementa el puerto del repositorio.
 * Aquí están las cuentas de ejemplo para el desafío.
 */
@Repository
public class CuentaEnMemoria implements CuentaRepository {

    private static final List<Cuenta> CUENTAS = List.of(
            new Cuenta(new NumeroCuenta("123456"), "Juan Pérez",
                    new BigDecimal("1500000"), new BigDecimal("500000"),
                    Cuenta.Estado.ACTIVA, LocalDate.of(2020, 3, 15)),
            new Cuenta(new NumeroCuenta("999999"), "María González",
                    BigDecimal.ZERO, BigDecimal.ZERO,
                    Cuenta.Estado.BLOQUEADA, LocalDate.of(2018, 11, 2)),
            new Cuenta(new NumeroCuenta("555555"), "Pedro Soto",
                    new BigDecimal("80000"), BigDecimal.ZERO,
                    Cuenta.Estado.ACTIVA, LocalDate.of(2022, 7, 1)),
            new Cuenta(new NumeroCuenta("777000"), "Ana Rojas",
                    new BigDecimal("240000"), new BigDecimal("100000"),
                    Cuenta.Estado.CERRADA, LocalDate.of(2019, 1, 20))
    );

    @Override
    public Optional<Cuenta> buscarPor(NumeroCuenta numero) {
        return CUENTAS.stream()
                .filter(c -> c.numero().valor().equals(numero.valor()))
                .findFirst();
    }

    @Override
    public List<Cuenta> listarPorEstado(Cuenta.Estado estado) {
        return CUENTAS.stream()
                .filter(c -> c.estado() == estado)
                .toList();
    }
}
