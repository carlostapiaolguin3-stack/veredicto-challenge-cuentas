package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.input.ConsultarSaldoUfUseCase;
import com.challenge.cuentas.application.ports.input.ListarCuentasPorEstadoUseCase;
import com.challenge.cuentas.domain.exception.CuentaNotFoundException;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import com.challenge.cuentas.domain.model.SaldoUf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConsultarCuentaUseCase consultarCuenta;

    @MockBean
    private ListarCuentasPorEstadoUseCase listarCuentasPorEstado;

    @MockBean
    private ConsultarSaldoUfUseCase consultarSaldoUf;

    @Test
    @DisplayName("GET cuenta existente devuelve 200")
    void cuentaExistente() throws Exception {
        Cuenta cuenta = new Cuenta(new NumeroCuenta("123456"), "Juan",
                new BigDecimal("1500000"), new BigDecimal("500000"),
                Cuenta.Estado.ACTIVA, LocalDate.of(2020, 3, 15));

        when(consultarCuenta.consultar(any())).thenReturn(cuenta);

        mvc.perform(get("/api/v1/cuentas/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value("123456"))
                .andExpect(jsonPath("$.estado").value("ACTIVA"));
    }

    @Test
    @DisplayName("GET cuenta inexistente devuelve 404")
    void cuentaInexistente() throws Exception {
        when(consultarCuenta.consultar(any()))
                .thenThrow(new CuentaNotFoundException(new NumeroCuenta("000000")));

        mvc.perform(get("/api/v1/cuentas/000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET con número inválido devuelve 400")
    void numeroInvalido() throws Exception {
        mvc.perform(get("/api/v1/cuentas/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET cuentas por estado devuelve 200")
    void listarCuentasPorEstado() throws Exception {
        Cuenta cuenta1 = new Cuenta(new NumeroCuenta("123456"), "Juan",
                new BigDecimal("1500000"), new BigDecimal("500000"),
                Cuenta.Estado.ACTIVA, LocalDate.of(2020, 3, 15));

        Cuenta cuenta2 = new Cuenta(new NumeroCuenta("555555"), "Pedro",
                new BigDecimal("80000"), BigDecimal.ZERO,
                Cuenta.Estado.ACTIVA, LocalDate.of(2022, 7, 1));

        when(listarCuentasPorEstado.listarPorEstado(Cuenta.Estado.ACTIVA))
                .thenReturn(List.of(cuenta1, cuenta2));

        mvc.perform(get("/api/v1/cuentas").param("estado", "ACTIVA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numero").value("123456"))
                .andExpect(jsonPath("$[0].estado").value("ACTIVA"))
                .andExpect(jsonPath("$[1].numero").value("555555"))
                .andExpect(jsonPath("$[1].estado").value("ACTIVA"));
    }

    @Test
    @DisplayName("GET saldo en UF devuelve 200")
    void consultarSaldoUf() throws Exception {
        SaldoUf saldoUf = new SaldoUf(
                new NumeroCuenta("123456"),
                new BigDecimal("2000000"),
                new BigDecimal("40000"),
                new BigDecimal("50.00")
        );

        when(consultarSaldoUf.consultarSaldoUf(any())).thenReturn(saldoUf);

        mvc.perform(get("/api/v1/cuentas/123456/saldo-uf"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("123456"))
                .andExpect(jsonPath("$.saldoDisponible").value(2000000))
                .andExpect(jsonPath("$.valorUf").value(40000))
                .andExpect(jsonPath("$.saldoEnUf").value(50.00));
    }
}