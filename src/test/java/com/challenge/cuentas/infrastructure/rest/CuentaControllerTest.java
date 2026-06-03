package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.domain.exception.CuentaNotFoundException;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

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
}
