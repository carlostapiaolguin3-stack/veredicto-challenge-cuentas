package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.input.ConsultarSaldoUfUseCase;
import com.challenge.cuentas.application.ports.input.ListarCuentasPorEstadoUseCase;
import com.challenge.cuentas.domain.exception.CuentaNotFoundException;
import com.challenge.cuentas.domain.exception.ServicioExternoNoDisponibleException;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    private ConsultarCuentaUseCase consultarCuenta;

    @MockitoBean
    private ListarCuentasPorEstadoUseCase listarPorEstado;

    @MockitoBean
    private ConsultarSaldoUfUseCase consultarSaldoUf;

    // ── F1: GET /{numero} ────────────────────────────────────────────────────

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

    // ── F2: GET ?estado= ─────────────────────────────────────────────────────

    @Test
    @DisplayName("GET ?estado=ACTIVA devuelve lista de cuentas activas")
    void listarPorEstadoActiva() throws Exception {
        Cuenta cuenta = new Cuenta(new NumeroCuenta("123456"), "Juan",
                new BigDecimal("1500000"), new BigDecimal("500000"),
                Cuenta.Estado.ACTIVA, LocalDate.of(2020, 3, 15));
        when(listarPorEstado.listar(Cuenta.Estado.ACTIVA)).thenReturn(List.of(cuenta));

        mvc.perform(get("/api/v1/cuentas").param("estado", "ACTIVA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numero").value("123456"))
                .andExpect(jsonPath("$[0].estado").value("ACTIVA"));
    }

    @Test
    @DisplayName("GET ?estado=invalido devuelve 400")
    void listarPorEstadoInvalido() throws Exception {
        mvc.perform(get("/api/v1/cuentas").param("estado", "INEXISTENTE"))
                .andExpect(status().isBadRequest());
    }

    // ── F3: GET /{numero}/saldo-uf ────────────────────────────────────────────

    @Test
    @DisplayName("GET saldo-uf devuelve 200 con conversión")
    void saldoUfExistente() throws Exception {
        ConsultarSaldoUfUseCase.Resultado resultado = new ConsultarSaldoUfUseCase.Resultado(
                "123456", new BigDecimal("2000000"), new BigDecimal("37000"), new BigDecimal("54.0541"));
        when(consultarSaldoUf.consultar(any())).thenReturn(resultado);

        mvc.perform(get("/api/v1/cuentas/123456/saldo-uf"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value("123456"))
                .andExpect(jsonPath("$.saldoDisponible").value(2000000))
                .andExpect(jsonPath("$.uf").value(37000))
                .andExpect(jsonPath("$.saldoEnUf").value(54.0541));
    }

    @Test
    @DisplayName("GET saldo-uf devuelve 503 cuando el servicio externo falla")
    void saldoUfServicioNoDisponible() throws Exception {
        when(consultarSaldoUf.consultar(any()))
                .thenThrow(new ServicioExternoNoDisponibleException("mindicador"));

        mvc.perform(get("/api/v1/cuentas/123456/saldo-uf"))
                .andExpect(status().isServiceUnavailable());
    }
}
