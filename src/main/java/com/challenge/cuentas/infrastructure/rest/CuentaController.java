package com.challenge.cuentas.infrastructure.rest;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.output.CuentaRepository;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cuentas")
public class CuentaController {

    private final ConsultarCuentaUseCase consultarCuenta;
    private final CuentaRepository repo;

    public CuentaController(ConsultarCuentaUseCase consultarCuenta, CuentaRepository repo) {
        this.consultarCuenta = consultarCuenta;
        this.repo = repo;
    }

    @GetMapping("/{numero}")
    public ResponseEntity<CuentaResponse> consultar(@PathVariable String numero) {
        Cuenta cuenta = consultarCuenta.consultar(new NumeroCuenta(numero));
        return ResponseEntity.ok(CuentaResponse.from(cuenta));
    }

    @GetMapping
    public List<Cuenta> listar(@RequestParam String estado) {
        return repo.listarPorEstado(Cuenta.Estado.valueOf(estado));
    }
}
