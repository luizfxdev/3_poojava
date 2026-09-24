package br.com.empresa.contratos.pagamento;

import java.math.BigDecimal;

public class ServicoDePagamentoPaypal implements ServicoDePagamentoOnline {

    private static final BigDecimal JURO_MENSAL_PADRAO = new BigDecimal("0.01");
    private static final BigDecimal TAXA_DE_PAGAMENTO_PADRAO = new BigDecimal("0.02");

    private final BigDecimal juroMensal;
    private final BigDecimal taxaDePagamento;

    public ServicoDePagamentoPaypal() {
        this(JURO_MENSAL_PADRAO, TAXA_DE_PAGAMENTO_PADRAO);
    }

    public ServicoDePagamentoPaypal(BigDecimal juroMensal, BigDecimal taxaDePagamento) {
        if (juroMensal == null || juroMensal.signum() < 0) {
            throw new IllegalArgumentException("O juro mensal nao pode ser negativo.");
        }
        if (taxaDePagamento == null || taxaDePagamento.signum() < 0) {
            throw new IllegalArgumentException("A taxa de pagamento nao pode ser negativa.");
        }
        this.juroMensal = juroMensal;
        this.taxaDePagamento = taxaDePagamento;
    }

    @Override
    public BigDecimal calcularJuros(BigDecimal valorBase, int quantidadeDeMeses) {
        if (valorBase == null || valorBase.signum() <= 0) {
            throw new IllegalArgumentException("O valor base deve ser maior que zero.");
        }
        if (quantidadeDeMeses <= 0) {
            throw new IllegalArgumentException("A quantidade de meses deve ser maior que zero.");
        }
        return valorBase.multiply(juroMensal).multiply(BigDecimal.valueOf(quantidadeDeMeses));
    }

    @Override
    public BigDecimal calcularTaxaDePagamento(BigDecimal valorComJuros) {
        if (valorComJuros == null || valorComJuros.signum() <= 0) {
            throw new IllegalArgumentException("O valor com juros deve ser maior que zero.");
        }
        return valorComJuros.multiply(taxaDePagamento);
    }

    @Override
    public String nome() {
        return "Paypal";
    }
}