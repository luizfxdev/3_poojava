package exercicio3;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hospedagem hospedagem = new Hospedagem();

        System.out.print("Quantos campeões serão alojados? (1-10): ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        if (quantidade < 1 || quantidade > 10) {
            System.out.println("Erro: Digite um número entre 1 e 10!");
            scanner.close();
            return;
        }

        for (int i = 0; i < quantidade; i++) {
            System.out.println("\n--- Campeão " + (i + 1) + " ---");

            System.out.print("Nome do Campeão: ");
            String nome = scanner.nextLine();

            System.out.print("Função/Rota (Top, Jungle, Mid, ADC, SUp): ");
            String funcao = scanner.nextLine();

            System.out.print("Número do Quarto (0-9): ");
            int quarto = Integer.parseInt(scanner.nextLine());

            hospedagem.registrarCampeao(nome, funcao, quarto);
        }

        hospedagem.exibirTodos();

        int ocupados = hospedagem.contarOcupados();
        System.out.printf("Total de quartos ocupados %d/10%n", ocupados);

        scanner.close();
    }
}
