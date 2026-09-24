package br.com.empresa.contratos.aplicacao;

import br.com.empresa.contratos.dominio.Contrato;
import br.com.empresa.contratos.entrada.EntradaInvalidaException;
import br.com.empresa.contratos.entrada.LeitorDeDadosDoContrato;
import br.com.empresa.contratos.entrada.LeitorDeDadosDoContratoConsole;
import br.com.empresa.contratos.pagamento.ServicoDePagamentoOnline;
import br.com.empresa.contratos.pagamento.ServicoDePagamentoPaypal;
import br.com.empresa.contratos.processamento.ProcessadorDeContrato;
import br.com.empresa.contratos.processamento.ProcessadorDeContratoPadrao;
import br.com.empresa.contratos.saida.ApresentadorDeParcelas;
import br.com.empresa.contratos.saida.ApresentadorDeParcelasConsole;

import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            LeitorDeDadosDoContrato leitor = new LeitorDeDadosDoContratoConsole(scanner, System.out);
            ServicoDePagamentoOnline servico = new ServicoDePagamentoPaypal();
            ProcessadorDeContrato processador = new ProcessadorDeContratoPadrao(servico);
            ApresentadorDeParcelas apresentador = new ApresentadorDeParcelasConsole(System.out);

            try {
                Contrato contrato = leitor.lerContrato();
                int quantidade = leitor.lerQuantidadeDeParcelas();
                processador.processar(contrato, quantidade);
                apresentador.apresentar(contrato);
            } catch (EntradaInvalidaException | IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}
