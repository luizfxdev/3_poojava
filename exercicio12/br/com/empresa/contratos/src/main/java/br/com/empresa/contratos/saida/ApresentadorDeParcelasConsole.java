package br.com.empresa.contratos.saida;

import br.com.empresa.contratos.dominio.Contrato;
import br.com.empresa.contratos.dominio.Parcela;

import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class ApresentadorDeParcelasConsole implements ApresentadorDeParcelas {

    private static final DateTimeFormatter FORMATO_DE_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PrintStream saida;

    public ApresentadorDeParcelasConsole(PrintStream saida) {
        this.saida = Objects.requireNonNull(saida, "A saida e obrigatoria.");
    }

    @Override
    public void apresentar(Contrato contrato) {
        Objects.requireNonNull(contrato, "O contrato e obrigatorio.");
        saida.println("Parcelas:");
        for (Parcela parcela : contrato.getParcelas()) {
            saida.println(formatar(parcela));
        }
    }

    private String formatar(Parcela parcela) {
        return String.format(
                Locale.US,
                "%s - %.2f",
                parcela.vencimento().format(FORMATO_DE_DATA),
                parcela.valor());
    }
}