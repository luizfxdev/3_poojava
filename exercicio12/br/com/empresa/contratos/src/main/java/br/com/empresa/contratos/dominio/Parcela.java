package br.com.empresa.contratos.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record Parcela(LocalDate vencimento, BigDecimal valor) {

    public Parcela {
        Objects.requireNonNull(vencimento, "O vencimento da parcela e obrigatorio.");
        Objects.requireNonNull(valor, "O valor da parcela e obrigatorio.");
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor da parcela deve ser maior que zero.");
        }
    }
}