package br.com.empresa.contratos.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Contrato {

    private final Integer numero;
    private final LocalDate data;
    private final BigDecimal valorTotal;
    private final List<Parcela> parcelas = new ArrayList<>();

    public Contrato(Integer numero, LocalDate data, BigDecimal valorTotal) {
        Objects.requireNonNull(numero, "O numero do contrato e obrigatorio.");
        Objects.requireNonNull(data, "A data do contrato e obrigatoria.");
        Objects.requireNonNull(valorTotal, "O valor do contrato e obrigatorio.");
        if (numero <= 0) {
            throw new IllegalArgumentException("O numero do contrato deve ser maior que zero.");
        }
        if (valorTotal.signum() <= 0) {
            throw new IllegalArgumentException("O valor do contrato deve ser maior que zero.");
        }
        this.numero = numero;
        this.data = data;
        this.valorTotal = valorTotal;
    }

    public Integer getNumero() {
        return numero;
    }

    public LocalDate getData() {
        return data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public List<Parcela> getParcelas() {
        return Collections.unmodifiableList(parcelas);
    }

    public void adicionarParcela(Parcela parcela) {
        Objects.requireNonNull(parcela, "A parcela e obrigatoria.");
        parcelas.add(parcela);
    }

    public void limparParcelas() {
        parcelas.clear();
    }
}