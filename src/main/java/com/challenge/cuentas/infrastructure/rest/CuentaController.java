package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.input.ListarCuentasPorEstadoUseCase;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Adapter de entrada REST. Traduce HTTP a/desde los casos de uso.
 * No conoce el repositorio ni la fuente de datos.
 */
@RestController
@RequestMapping("/api/v1/cuentas")
public class CuentaController {

    private final ConsultarCuentaUseCase consultarCuenta;
    private final ListarCuentasPorEstadoUseCase listarCuentas;

    public CuentaController(ConsultarCuentaUseCase consultarCuenta,
                            ListarCuentasPorEstadoUseCase listarCuentas) {
        this.consultarCuenta = consultarCuenta;
        this.listarCuentas = listarCuentas;
    }

    @GetMapping("/{numero}")
    public ResponseEntity<CuentaResponse> consultar(@PathVariable String numero) {
        Cuenta cuenta = consultarCuenta.consultar(new NumeroCuenta(numero));
        return ResponseEntity.ok(CuentaResponse.from(cuenta));
    }

    // F1 · saldo disponible
    @GetMapping("/{numero}/saldo")
    public ResponseEntity<SaldoResponse> saldo(@PathVariable String numero) {
        Cuenta cuenta = consultarCuenta.consultar(new NumeroCuenta(numero));
        return ResponseEntity.ok(new SaldoResponse(cuenta.numero().valor(), cuenta.saldoDisponible()));
    }

    // F2 · listar por estado
    @GetMapping
    public ResponseEntity<List<CuentaResponse>> listar(@RequestParam String estado) {
        Cuenta.Estado estadoValido = parseEstado(estado);
        List<CuentaResponse> cuentas = listarCuentas.listar(estadoValido).stream()
                .map(CuentaResponse::from)
                .toList();
        return ResponseEntity.ok(cuentas);
    }

    private Cuenta.Estado parseEstado(String estado) {
        try {
            return Cuenta.Estado.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("estado inválido: " + estado + " (use ACTIVA, BLOQUEADA o CERRADA)");
        }
    }
}
