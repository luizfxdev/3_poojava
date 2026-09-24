package br.com.empresa.contratos.processamento;

import br.com.empresa.contratos.dominio.Contrato;
import br.com.empresa.contratos.dominio.Parcela;
import br.com.empresa.contratos.pagamento.ServicoDePagamentoPaypal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessadorDeContratoPadraoTest {

    private final ProcessadorDeContrato processador =
            new ProcessadorDeContratoPadrao(new ServicoDePagamentoPaypal());

    @Test
    void deveReproduzirExemploDoEnunciado() {
        Contrato contrato = new Contrato(8028, LocalDate.of(2018, 6, 25), new BigDecimal("600.00"));
        processador.processar(contrato, 3);

        List<Parcela> parcelas = contrato.getParcelas();
        assertEquals(3, parcelas.size());
        assertEquals(LocalDate.of(2018, 7, 25), parcelas.get(0).vencimento());
        assertEquals(LocalDate.of(2018, 8, 25), parcelas.get(1).vencimento());
        assertEquals(LocalDate.of(2018, 9, 25), parcelas.get(2).vencimento());
        assertEquals(new BigDecimal("206.04"), parcelas.get(0).valor());
        assertEquals(new BigDecimal("208.08"), parcelas.get(1).valor());
        assertEquals(new BigDecimal("210.12"), parcelas.get(2).valor());
    }

    @Test
    void vencimentoEmMesCurtoUsaUltimoDiaDoMes() {
        Contrato contrato = new Contrato(1, LocalDate.of(2023, 1, 31), new BigDecimal("100.00"));
        processador.processar(contrato, 1);
        assertEquals(LocalDate.of(2023, 2, 28), contrato.getParcelas().get(0).vencimento());
    }

    @Test
    void reprocessarSubstituiParcelasAnteriores() {
        Contrato contrato = new Contrato(1, LocalDate.of(2024, 1, 10), new BigDecimal("300.00"));
        processador.processar(contrato, 5);
        processador.processar(contrato, 2);
        assertEquals(2, contrato.getParcelas().size());
    }

    @Test
    void quantidadeDeParcelasInvalidaLancaExcecao() {
        Contrato contrato = new Contrato(1, LocalDate.of(2024, 1, 10), new BigDecimal("300.00"));
        assertThrows(IllegalArgumentException.class, () -> processador.processar(contrato, 0));
        assertThrows(IllegalArgumentException.class, () -> processador.processar(contrato, -3));
    }

    @Test
    void jurosSimplesSaoProporcionaisAoNumeroDeMeses() {
        BigDecimal total = new BigDecimal("1200.00");
        int n = 4;
        Contrato contrato = new Contrato(1, LocalDate.of(2024, 1, 10), total);
        processador.processar(contrato, n);

        BigDecimal base = total.divide(BigDecimal.valueOf(n));
        BigDecimal esperada = base.add(base.multiply(new BigDecimal("0.01")).multiply(new BigDecimal("2")))
                .multiply(new BigDecimal("1.02"))
                .setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(esperada, contrato.getParcelas().get(1).valor());
    }

    @Test
    void listaDeParcelasEhImutavel() {
        Contrato contrato = new Contrato(1, LocalDate.of(2024, 1, 10), new BigDecimal("100.00"));
        assertThrows(UnsupportedOperationException.class, () -> contrato.getParcelas().add(null));
    }
}
