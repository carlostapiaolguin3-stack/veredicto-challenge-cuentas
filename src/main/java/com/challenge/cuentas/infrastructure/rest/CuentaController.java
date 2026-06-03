package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adapter de entrada REST. Traduce HTTP a/desde el caso de uso.
 * No conoce el repositorio ni la fuente de datos.
 */
@RestController
@RequestMapping("/api/v1/cuentas")
public class CuentaController {

    private final ConsultarCuentaUseCase consultarCuenta;

    public CuentaController(ConsultarCuentaUseCase consultarCuenta) {
        this.consultarCuenta = consultarCuenta;
    }

    @GetMapping("/{numero}")
    public ResponseEntity<CuentaResponse> consultar(@PathVariable String numero) {
        Cuenta cuenta = consultarCuenta.consultar(new NumeroCuenta(numero));
        return ResponseEntity.ok(CuentaResponse.from(cuenta));
    }
}
