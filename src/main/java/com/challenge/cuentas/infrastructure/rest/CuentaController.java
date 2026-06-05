package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.input.ConsultarSaldoUfUseCase;
import com.challenge.cuentas.application.ports.input.ListarCuentasPorEstadoUseCase;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adapter de entrada REST. Traduce HTTP a/desde el caso de uso.
 * No conoce el repositorio ni la fuente de datos.
 */
@RestController
@RequestMapping("/api/v1/cuentas")
public class CuentaController {

    private final ConsultarCuentaUseCase consultarCuenta;
    private final ListarCuentasPorEstadoUseCase listarPorEstado;
    private final ConsultarSaldoUfUseCase consultarSaldoUf;

    public CuentaController(ConsultarCuentaUseCase consultarCuenta,
                            ListarCuentasPorEstadoUseCase listarPorEstado,
                            ConsultarSaldoUfUseCase consultarSaldoUf) {
        this.consultarCuenta = consultarCuenta;
        this.listarPorEstado = listarPorEstado;
        this.consultarSaldoUf = consultarSaldoUf;
    }

    @GetMapping("/{numero}")
    public ResponseEntity<CuentaResponse> consultar(@PathVariable String numero) {
        Cuenta cuenta = consultarCuenta.consultar(new NumeroCuenta(numero));
        return ResponseEntity.ok(CuentaResponse.from(cuenta));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> listarPorEstado(@RequestParam String estado) {
        Cuenta.Estado estadoEnum = Cuenta.Estado.valueOf(estado.toUpperCase());
        List<CuentaResponse> respuesta = listarPorEstado.listar(estadoEnum)
                .stream().map(CuentaResponse::from).toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{numero}/saldo-uf")
    public ResponseEntity<SaldoUfResponse> saldoUf(@PathVariable String numero) {
        ConsultarSaldoUfUseCase.Resultado resultado = consultarSaldoUf.consultar(new NumeroCuenta(numero));
        return ResponseEntity.ok(SaldoUfResponse.from(resultado));
    }
}
