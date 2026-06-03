package com.challenge.cuentas.domain.model;

/**
 * Value Object del número de cuenta. Encapsula la regla de formato en un tipo,
 * en vez de pasar Strings sueltos por el código.
 */
public record NumeroCuenta(String valor) {

    public NumeroCuenta {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("el número de cuenta no puede ser vacío");
        }
        if (!valor.matches("\\d{6,20}")) {
            throw new IllegalArgumentException(
                "número de cuenta inválido: solo dígitos, entre 6 y 20 caracteres");
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
