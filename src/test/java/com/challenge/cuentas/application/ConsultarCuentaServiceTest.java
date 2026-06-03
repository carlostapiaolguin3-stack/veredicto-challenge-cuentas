package com.challenge.cuentas.application;

import com.challenge.cuentas.application.ports.output.CuentaRepository;
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

    // Repositorio falso configurable para los tests del caso de uso.
    private static CuentaRepository repoCon(Optional<Cuenta> resultado) {
        return new CuentaRepository() {
            @Override
            public Optional<Cuenta> buscarPor(NumeroCuenta numero) {
                return resultado;
            }

            @Override
            public List<Cuenta> listarPorEstado(Cuenta.Estado estado) {
                return List.of();
            }
        };
    }

    @Test
    @DisplayName("devuelve la cuenta cuando existe")
    void devuelveCuentaCuandoExiste() {
        Cuenta cuenta = new Cuenta(new NumeroCuenta("123456"), "Test",
                new BigDecimal("100"), BigDecimal.ZERO, Cuenta.Estado.ACTIVA, LocalDate.now());
        ConsultarCuentaService service = new ConsultarCuentaService(repoCon(Optional.of(cuenta)));

        Cuenta resultado = service.consultar(new NumeroCuenta("123456"));

        assertEquals("123456", resultado.numero().valor());
    }

    @Test
    @DisplayName("lanza CuentaNotFoundException cuando no existe")
    void lanzaNotFoundCuandoNoExiste() {
        ConsultarCuentaService service = new ConsultarCuentaService(repoCon(Optional.empty()));

        assertThrows(CuentaNotFoundException.class,
                () -> service.consultar(new NumeroCuenta("000000")));
    }
}
