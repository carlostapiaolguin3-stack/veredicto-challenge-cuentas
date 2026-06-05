package com.challenge.cuentas.application;

import com.challenge.cuentas.application.ports.output.CuentaRepository;
import com.challenge.cuentas.application.ports.output.IndicadorRepository;
import com.challenge.cuentas.application.service.ConsultarCuentaService;
import com.challenge.cuentas.domain.exception.CuentaNotFoundException;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConsultarCuentaServiceTest {

    @Test
    @DisplayName("devuelve la cuenta cuando existe")
    void devuelveCuentaCuandoExiste() {
        Cuenta cuenta = new Cuenta(new NumeroCuenta("123456"), "Test",
                new BigDecimal("100"), BigDecimal.ZERO,
                Cuenta.Estado.ACTIVA, LocalDate.now());

        CuentaRepository repo = new CuentaRepository() {
            @Override
            public Optional<Cuenta> buscarPor(NumeroCuenta numero) {
                return Optional.of(cuenta);
            }

            @Override
            public List<Cuenta> buscarPorEstado(Cuenta.Estado estado) {
                return List.of(cuenta);
            }
        };

        IndicadorRepository indicadorRepository = () -> new BigDecimal("40000");

        ConsultarCuentaService service = new ConsultarCuentaService(repo, indicadorRepository);

        Cuenta resultado = service.consultar(new NumeroCuenta("123456"));

        assertEquals("123456", resultado.numero().valor());
    }

    @Test
    @DisplayName("lanza CuentaNotFoundException cuando no existe")
    void lanzaNotFoundCuandoNoExiste() {
        CuentaRepository repo = new CuentaRepository() {
            @Override
            public Optional<Cuenta> buscarPor(NumeroCuenta numero) {
                return Optional.empty();
            }

            @Override
            public List<Cuenta> buscarPorEstado(Cuenta.Estado estado) {
                return List.of();
            }
        };

        IndicadorRepository indicadorRepository = () -> new BigDecimal("40000");

        ConsultarCuentaService service = new ConsultarCuentaService(repo, indicadorRepository);

        assertThrows(CuentaNotFoundException.class,
                () -> service.consultar(new NumeroCuenta("000000")));
    }

    @Test
    @DisplayName("calcula saldo disponible convertido a UF")
    void calculaSaldoDisponibleEnUf() {
        Cuenta cuenta = new Cuenta(new NumeroCuenta("123456"), "Test",
                new BigDecimal("1500000"), new BigDecimal("500000"),
                Cuenta.Estado.ACTIVA, LocalDate.now());

        CuentaRepository repo = new CuentaRepository() {
            @Override
            public Optional<Cuenta> buscarPor(NumeroCuenta numero) {
                return Optional.of(cuenta);
            }

            @Override
            public List<Cuenta> buscarPorEstado(Cuenta.Estado estado) {
                return List.of(cuenta);
            }
        };

        IndicadorRepository indicadorRepository = () -> new BigDecimal("40000");

        ConsultarCuentaService service = new ConsultarCuentaService(repo, indicadorRepository);

        var resultado = service.consultarSaldoUf(new NumeroCuenta("123456"));

        assertEquals("123456", resultado.numeroCuenta().valor());
        assertEquals(new BigDecimal("2000000"), resultado.saldoDisponible());
        assertEquals(new BigDecimal("40000"), resultado.valorUf());
        assertEquals(new BigDecimal("50.00"), resultado.saldoEnUf());
    }
}