package br.com.empresa.contratos.pagamento;

import java.math.BigDecimal;

public interface ServicoDePagamentoOnline {

    BigDecimal calcularJuros(BigDecimal valorBase, int quantidadeDeMeses);

    BigDecimal calcularTaxaDePagamento(BigDecimal valorComJuros);

    String nome();
}