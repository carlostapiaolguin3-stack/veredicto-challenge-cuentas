package com.challenge.cuentas.infrastructure.external;

import com.challenge.cuentas.application.ports.output.IndicadoresPort;
import com.challenge.cuentas.domain.exception.ServicioExternoNoDisponibleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MindicadorAdapter implements IndicadoresPort {

    private final RestClient restClient;

    public MindicadorAdapter(
            @Value("${indicadores.uf-url}") String ufUrl,
            @Value("${indicadores.timeout-ms}") int timeoutMs) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeoutMs);
        factory.setReadTimeout(timeoutMs);
        this.restClient = RestClient.builder()
                .baseUrl(ufUrl)
                .requestFactory(factory)
                .build();
    }

    @Override
    public BigDecimal obtenerValorUF() {
        try {
            MindicadorResponse response = restClient.get()
                    .retrieve()
                    .body(MindicadorResponse.class);
            if (response == null || response.serie() == null || response.serie().isEmpty()) {
                throw new ServicioExternoNoDisponibleException("mindicador");
            }
            return response.serie().get(0).valor();
        } catch (RestClientException e) {
            throw new ServicioExternoNoDisponibleException("mindicador");
        }
    }

    private record MindicadorResponse(List<SerieItem> serie) {}

    private record SerieItem(BigDecimal valor) {}
}
