package com.challenge.cuentas.application;

import com.challenge.cuentas.application.ports.output.CuentaRepository;
import com.challenge.cuentas.application.service.ListarCuentasPorEstadoService;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarCuentasPorEstadoServiceTest {

    @Test
    @DisplayName("devuelve cuentas filtradas por estado ACTIVA")
    void devuelveCuentasActivasPorEstado() {
        Cuenta cuenta = new Cuenta(new NumeroCuenta("123456"), "Test",
                new BigDecimal("100"), BigDecimal.ZERO, Cuenta.Estado.ACTIVA, LocalDate.now());
        CuentaRepository repo = mock(CuentaRepository.class);
        when(repo.buscarPorEstado(Cuenta.Estado.ACTIVA)).thenReturn(List.of(cuenta));
        ListarCuentasPorEstadoService service = new ListarCuentasPorEstadoService(repo);

        List<Cuenta> resultado = service.listar(Cuenta.Estado.ACTIVA);

        assertEquals(1, resultado.size());
        assertEquals(Cuenta.Estado.ACTIVA, resultado.get(0).estado());
    }

    @Test
    @DisplayName("devuelve lista vacía si no hay cuentas en ese estado")
    void devuelveListaVaciaSiNoHayCuentas() {
        CuentaRepository repo = mock(CuentaRepository.class);
        when(repo.buscarPorEstado(Cuenta.Estado.CERRADA)).thenReturn(List.of());
        ListarCuentasPorEstadoService service = new ListarCuentasPorEstadoService(repo);

        List<Cuenta> resultado = service.listar(Cuenta.Estado.CERRADA);

        assertTrue(resultado.isEmpty());
    }
}
