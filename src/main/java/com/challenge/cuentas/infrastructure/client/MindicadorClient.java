package com.challenge.cuentas.infrastructure.client;

import com.challenge.cuentas.application.ports.output.IndicadorRepository;
import com.challenge.cuentas.domain.exception.IndicadorNoDisponibleException;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class MindicadorClient implements IndicadorRepository {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public BigDecimal obtenerValorUf() {
        try {
            String url = "https://mindicador.cl/api/uf";

            Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);

            List<Map<String, Object>> serie = (List<Map<String, Object>>) respuesta.get("serie");

            Number valorUf = (Number) serie.get(0).get("valor");

            return BigDecimal.valueOf(valorUf.doubleValue());
    } catch (Exception e) {
        throw new IndicadorNoDisponibleException();
        }
    }
}      