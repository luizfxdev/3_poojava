public class ContaBancaria {
    private final int numero;
    private String titular;
    private double saldo;
    private static final double TAXA_SAQUE = 5.00;

    // Construtor 1: com depósito inicial
    public ContaBancaria(int numero, String titular, double depositoInicial) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = depositoInicial;
    }

    // Construtor 2: sem depósito inicial (sobrecarga)
    public ContaBancaria(int numero, String titular) {
        this(numero, titular, 0.0);
    }

    public void depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
            System.out.printf("Depósito de R$ %.2f realizado com sucesso%n", valor);
        } else {
            System.out.println("Valor de depósito deve ser positivo");
        }
    }

    public void sacar(double valor) {
        if (valor > 0) {
            double total = valor + TAXA_SAQUE;
            saldo -= total;
            System.out.printf("Saque de R$ %.2f realizado (taxa R$ %.2f)%n", valor, TAXA_SAQUE);
        } else {
            System.out.println("Valor de saque deve ser positivo");
        }
    }

    public void alterarTitular(String novoNome) {
        this.titular = novoNome;
    }

    public void exibir() {
        System.out.println("=== Dados da Conta ===");
        System.out.printf("Número: %d%n", numero);
        System.out.printf("Titular: %s%n", titular);
        System.out.printf("Saldo: R$ %.2f%n", saldo);
        System.out.println();
    }

    // Getters (encapsulamento)
    public int getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }
}