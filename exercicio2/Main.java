public class Main {
    public static void main(String[] args) {
        // Teste 1: Com depósito inicial
        ContaBancaria conta1 = new ContaBancaria(1234, "João Silva", 1000.00);
        conta1.exibir();

        conta1.depositar(500.00);
        conta1.exibir();

        conta1.sacar(200.00);
        conta1.exibir();

        conta1.alterarTitular("João Silva Santos");
        conta1.exibir();

        System.out.println("\n--- Teste 2 ---\n");

        // Teste 2: Sem depósito inicial
        ContaBancaria conta2 = new ContaBancaria(5678, "Maria");
        conta2.exibir();

        conta2.depositar(750.00);
        conta2.exibir();

        conta2.sacar(1000.00); // Permite saldo negativo
        conta2.exibir();
    }
}