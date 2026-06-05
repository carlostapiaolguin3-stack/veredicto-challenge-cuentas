package com.challenge.cuentas.application;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.input.ConsultarSaldoUfUseCase;
import com.challenge.cuentas.application.ports.output.IndicadoresPort;
import com.challenge.cuentas.application.service.ConsultarSaldoUfService;
import com.challenge.cuentas.domain.exception.ServicioExternoNoDisponibleException;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConsultarSaldoUfServiceTest {

    @Test
    @DisplayName("convierte saldo disponible a UF correctamente")
    void convierteASaldoUf() {
        Cuenta cuenta = new Cuenta(new NumeroCuenta("123456"), "Test",
                new BigDecimal("100000"), BigDecimal.ZERO, Cuenta.Estado.ACTIVA, LocalDate.now());
        ConsultarCuentaUseCase consultarCuenta = mock(ConsultarCuentaUseCase.class);
        IndicadoresPort indicadores = mock(IndicadoresPort.class);
        when(consultarCuenta.consultar(any())).thenReturn(cuenta);
        when(indicadores.obtenerValorUF()).thenReturn(new BigDecimal("40000"));

        ConsultarSaldoUfService service = new ConsultarSaldoUfService(consultarCuenta, indicadores);
        ConsultarSaldoUfUseCase.Resultado resultado = service.consultar(new NumeroCuenta("123456"));

        assertEquals(new BigDecimal("2.5000"), resultado.saldoEnUf());
        assertEquals(new BigDecimal("100000"), resultado.saldoDisponible());
        assertEquals(new BigDecimal("40000"), resultado.valorUf());
    }

    @Test
    @DisplayName("propaga ServicioExternoNoDisponibleException cuando falla el indicador")
    void propagaExcepcionServicioExterno() {
        Cuenta cuenta = new Cuenta(new NumeroCuenta("123456"), "Test",
                new BigDecimal("100000"), BigDecimal.ZERO, Cuenta.Estado.ACTIVA, LocalDate.now());
        ConsultarCuentaUseCase consultarCuenta = mock(ConsultarCuentaUseCase.class);
        IndicadoresPort indicadores = mock(IndicadoresPort.class);
        when(consultarCuenta.consultar(any())).thenReturn(cuenta);
        when(indicadores.obtenerValorUF())
                .thenThrow(new ServicioExternoNoDisponibleException("mindicador"));

        ConsultarSaldoUfService service = new ConsultarSaldoUfService(consultarCuenta, indicadores);

        assertThrows(ServicioExternoNoDisponibleException.class,
                () -> service.consultar(new NumeroCuenta("123456")));
    }
}
