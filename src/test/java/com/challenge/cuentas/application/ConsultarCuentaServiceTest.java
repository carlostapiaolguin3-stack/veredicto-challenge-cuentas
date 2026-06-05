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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConsultarCuentaServiceTest {

    @Test
    @DisplayName("devuelve la cuenta cuando existe")
    void devuelveCuentaCuandoExiste() {
        Cuenta cuenta = new Cuenta(new NumeroCuenta("123456"), "Test",
                new BigDecimal("100"), BigDecimal.ZERO, Cuenta.Estado.ACTIVA, LocalDate.now());
        CuentaRepository repo = mock(CuentaRepository.class);
        when(repo.buscarPor(any())).thenReturn(Optional.of(cuenta));
        ConsultarCuentaService service = new ConsultarCuentaService(repo);

        Cuenta resultado = service.consultar(new NumeroCuenta("123456"));

        assertEquals("123456", resultado.numero().valor());
    }

    @Test
    @DisplayName("lanza CuentaNotFoundException cuando no existe")
    void lanzaNotFoundCuandoNoExiste() {
        CuentaRepository repo = mock(CuentaRepository.class);
        when(repo.buscarPor(any())).thenReturn(Optional.empty());
        ConsultarCuentaService service = new ConsultarCuentaService(repo);

        assertThrows(CuentaNotFoundException.class,
                () -> service.consultar(new NumeroCuenta("000000")));
    }
}
