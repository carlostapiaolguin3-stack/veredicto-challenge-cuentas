package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.input.ConsultarSaldoUfUseCase;
import com.challenge.cuentas.application.ports.input.ListarCuentasPorEstadoUseCase;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import com.challenge.cuentas.domain.model.SaldoUf;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adapter de entrada REST. Traduce HTTP a/desde el caso de uso.
 * No conoce el repositorio ni la fuente de datos.
 */
@RestController
@RequestMapping("/api/v1/cuentas")
public class CuentaController {

    private final ConsultarCuentaUseCase consultarCuenta;
    private final ListarCuentasPorEstadoUseCase listarCuentasPorEstado;
    private final ConsultarSaldoUfUseCase consultarSaldoUf;

    public CuentaController(ConsultarCuentaUseCase consultarCuenta,
            ListarCuentasPorEstadoUseCase listarCuentasPorEstado,
            ConsultarSaldoUfUseCase consultarSaldoUf
    ) {
        this.consultarCuenta = consultarCuenta;
        this.listarCuentasPorEstado = listarCuentasPorEstado;
        this.consultarSaldoUf = consultarSaldoUf;
    }

    @GetMapping("/{numero}")
    public ResponseEntity<CuentaResponse> consultar(@PathVariable String numero) {
        Cuenta cuenta = consultarCuenta.consultar(new NumeroCuenta(numero));
        return ResponseEntity.ok(CuentaResponse.from(cuenta));
    }

    @GetMapping(params = "estado")
    public ResponseEntity<List<CuentaResponse>> listarPorEstado(@RequestParam Cuenta.Estado estado) {
        List<Cuenta> cuentas = listarCuentasPorEstado.listarPorEstado(estado);
        return ResponseEntity.ok(cuentas.stream().map(CuentaResponse::from).toList());
    }

    @GetMapping("/{numero}/saldo-uf")
    public ResponseEntity<SaldoUfResponse> consultarSaldoUf(@PathVariable String numero) {
        SaldoUf saldoUf = consultarSaldoUf.consultarSaldoUf(new NumeroCuenta(numero));
        return ResponseEntity.ok(SaldoUfResponse.from(saldoUf));
    }
    

}
