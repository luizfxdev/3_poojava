package br.com.empresa.contratos.entrada;

import br.com.empresa.contratos.dominio.Contrato;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Objects;
import java.util.Scanner;

public class LeitorDeDadosDoContratoConsole implements LeitorDeDadosDoContrato {

    private static final DateTimeFormatter FORMATO_DE_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner entrada;
    private final PrintStream saida;

    public LeitorDeDadosDoContratoConsole(Scanner entrada, PrintStream saida) {
        this.entrada = Objects.requireNonNull(entrada, "A entrada e obrigatoria.");
        this.saida = Objects.requireNonNull(saida, "A saida e obrigatoria.");
    }

    @Override
    public Contrato lerContrato() {
        saida.println("Entre os dados do contrato:");
        saida.print("Numero: ");
        Integer numero = lerNumeroInteiro("numero do contrato");
        saida.print("Data (dd/MM/yyyy): ");
        LocalDate data = lerData();
        saida.print("Valor do contrato: ");
        BigDecimal valor = lerValorMonetario();
        return new Contrato(numero, data, valor);
    }

    @Override
    public int lerQuantidadeDeParcelas() {
        saida.print("Entre com o numero de parcelas: ");
        int quantidade = lerNumeroInteiro("numero de parcelas");
        if (quantidade <= 0) {
            throw new EntradaInvalidaException("O numero de parcelas deve ser maior que zero.");
        }
        return quantidade;
    }

    private Integer lerNumeroInteiro(String campo) {
        String texto = proximaLinha(campo);
        try {
            return Integer.valueOf(texto);
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException("O campo " + campo + " deve ser um numero inteiro.", e);
        }
    }

    private LocalDate lerData() {
        String texto = proximaLinha("data do contrato");
        try {
            return LocalDate.parse(texto, FORMATO_DE_DATA);
        } catch (DateTimeParseException e) {
            throw new EntradaInvalidaException("A data do contrato deve seguir o formato dd/MM/yyyy.", e);
        }
    }

    private BigDecimal lerValorMonetario() {
        String texto = proximaLinha("valor do contrato").replace(",", ".");
        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException("O valor do contrato deve ser um numero decimal.", e);
        }
    }

    private String proximaLinha(String campo) {
        if (!entrada.hasNextLine()) {
            throw new EntradaInvalidaException("Nao foi informado o campo " + campo + ".");
        }
        String texto = entrada.nextLine().trim();
        if (texto.isEmpty()) {
            throw new EntradaInvalidaException("O campo " + campo + " nao pode ser vazio.");
        }
        return texto;
    }
}