package br.com.empresa.contratos.processamento;

import br.com.empresa.contratos.dominio.Contrato;
import br.com.empresa.contratos.dominio.Parcela;
import br.com.empresa.contratos.pagamento.ServicoDePagamentoOnline;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class ProcessadorDeContratoPadrao implements ProcessadorDeContrato {

    private static final int ESCALA_DE_CALCULO = 10;
    private static final int ESCALA_MONETARIA = 2;
    private static final RoundingMode ARREDONDAMENTO = RoundingMode.HALF_UP;

    private final ServicoDePagamentoOnline servicoDePagamento;

    public ProcessadorDeContratoPadrao(ServicoDePagamentoOnline servicoDePagamento) {
        this.servicoDePagamento = Objects.requireNonNull(
                servicoDePagamento, "O servico de pagamento e obrigatorio.");
    }

    @Override
    public void processar(Contrato contrato, int quantidadeDeParcelas) {
        Objects.requireNonNull(contrato, "O contrato e obrigatorio.");
        if (quantidadeDeParcelas <= 0) {
            throw new IllegalArgumentException("A quantidade de parcelas deve ser maior que zero.");
        }

        contrato.limparParcelas();

        BigDecimal valorBase = calcularValorBase(contrato.getValorTotal(), quantidadeDeParcelas);

        for (int mes = 1; mes <= quantidadeDeParcelas; mes++) {
            BigDecimal juros = servicoDePagamento.calcularJuros(valorBase, mes);
            BigDecimal valorComJuros = valorBase.add(juros);
            BigDecimal taxa = servicoDePagamento.calcularTaxaDePagamento(valorComJuros);
            BigDecimal valorFinal = valorComJuros.add(taxa).setScale(ESCALA_MONETARIA, ARREDONDAMENTO);
            LocalDate vencimento = contrato.getData().plusMonths(mes);
            contrato.adicionarParcela(new Parcela(vencimento, valorFinal));
        }
    }

    private BigDecimal calcularValorBase(BigDecimal valorTotal, int quantidadeDeParcelas) {
        return valorTotal.divide(
                BigDecimal.valueOf(quantidadeDeParcelas), ESCALA_DE_CALCULO, ARREDONDAMENTO);
    }
}