package com.challenge.cuentas.application.service;

import com.challenge.cuentas.application.ports.input.ListarCuentasPorEstadoUseCase;
import com.challenge.cuentas.application.ports.output.CuentaRepository;
import com.challenge.cuentas.domain.model.Cuenta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarCuentasPorEstadoService implements ListarCuentasPorEstadoUseCase {

    private final CuentaRepository repositorio;

    public ListarCuentasPorEstadoService(CuentaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Cuenta> listar(Cuenta.Estado estado) {
        return repositorio.listarPorEstado(estado);
    }
}
