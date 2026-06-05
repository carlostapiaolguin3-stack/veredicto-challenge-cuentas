package com.challenge.cuentas.application.service;

import com.challenge.cuentas.application.ports.input.ConsultarCuentaUseCase;
import com.challenge.cuentas.application.ports.input.ConsultarSaldoUfUseCase;
import com.challenge.cuentas.application.ports.output.IndicadoresPort;
import com.challenge.cuentas.domain.model.Cuenta;
import com.challenge.cuentas.domain.model.NumeroCuenta;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ConsultarSaldoUfService implements ConsultarSaldoUfUseCase {

    private final ConsultarCuentaUseCase consultarCuenta;
    private final IndicadoresPort indicadores;

    public ConsultarSaldoUfService(ConsultarCuentaUseCase consultarCuenta, IndicadoresPort indicadores) {
        this.consultarCuenta = consultarCuenta;
        this.indicadores = indicadores;
    }

    @Override
    public Resultado consultar(NumeroCuenta numero) {
        Cuenta cuenta = consultarCuenta.consultar(numero);
        BigDecimal valorUf = indicadores.obtenerValorUF();
        BigDecimal saldoDisponible = cuenta.saldoDisponible();
        BigDecimal saldoEnUf = saldoDisponible.divide(valorUf, 4, RoundingMode.HALF_UP);
        return new Resultado(cuenta.numero().valor(), saldoDisponible, valorUf, saldoEnUf);
    }
}
