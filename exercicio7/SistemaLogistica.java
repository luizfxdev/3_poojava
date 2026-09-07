import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class SistemaLogistica {
    
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        
        // Cria frota de veículos
        List<Veiculo> frota = new ArrayList<>();
        
        // Adiciona veículos
        Caminhao c1 = new Caminhao("ABC-1234", 5000, 80, 5.0, 3);
        c1.carregarCarga(2500);  // 2.5 toneladas
        frota.add(c1);
        
        Motocicleta m1 = new Motocicleta("XYZ-5678", 50, 120, 3.0, 600, 150);
        frota.add(m1);
        
        Van v1 = new Van("VAN-9999", 1500, 100, 4.0, 5, 0.08, 6.50);
        frota.add(v1);
        
        Caminhao c2 = new Caminhao("DEF-5432", 8000, 70, 4.5, 2);
        c2.carregarCarga(4000);  // 4 toneladas
        frota.add(c2);
        
        // ========== MENU ==========
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n========== SISTEMA DE LOGÍSTICA ==========");
            System.out.println("1. Calcular custo de entrega");
            System.out.println("2. Listar frota");
            System.out.println("3. Listar entregas comparadas");
            System.out.println("4. Sair");
            System.out.print("Escolha: ");
            
            int opcao = sc.nextInt();
            
            switch (opcao) {
                case 1:
                    calcularCustoEntrega(sc, frota);
                    break;
                case 2:
                    listarFrota(frota);
                    break;
                case 3:
                    listarEntregasComparadas(sc, frota);
                    break;
                case 4:
                    continuar = false;
                    System.out.println("Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        
        sc.close();
    }
    
    // ========== MÉTODO 1: Calcular Custo ==========
    private static void calcularCustoEntrega(Scanner sc, List<Veiculo> frota) {
        System.out.println("\n--- CALCULAR CUSTO DE ENTREGA ---");
        
        System.out.print("Digite a distância (km): ");
        double distancia = sc.nextDouble();
        
        System.out.println("\nCustos por veículo:");
        for (Veiculo v : frota) {
            double custo = v.calcularCusto(distancia);
            double tempo = v.calcularTempoEstimado(distancia);
            
            System.out.printf("%s - Custo: R$ %.2f, Tempo: %.1f horas%n", 
                            v.getPlaca(), custo, tempo);
        }
    }
    
    // ========== MÉTODO 2: Listar Frota ==========
    private static void listarFrota(List<Veiculo> frota) {
        System.out.println("\n--- FROTA DISPONÍVEL ---");
        for (Veiculo v : frota) {
            System.out.println(v);
        }
    }
    
    // ========== MÉTODO 3: Entregar Comparadas ==========
    private static void listarEntregasComparadas(Scanner sc, List<Veiculo> frota) {
        System.out.println("\n--- SIMULAÇÃO DE ENTREGAS COMPARADAS ---");
        
        System.out.print("Digite a distância (km): ");
        double distancia = sc.nextDouble();
        
        System.out.printf("\nEntrega de %.1f km:%n%n", distancia);
        
        // Encontra melhor e pior custo
        double menorCusto = Double.MAX_VALUE;
        double maiorCusto = Double.MIN_VALUE;
        Veiculo melhorOpcao = null;
        Veiculo piorOpcao = null;
        
        double custoTotal = 0;
        
        for (Veiculo v : frota) {
            double custo = v.calcularCusto(distancia);
            custoTotal += custo;
            
            // Verifica se é melhor opção
            if (custo < menorCusto) {
                menorCusto = custo;
                melhorOpcao = v;
            }
            
            // Verifica se é pior opção
            if (custo > maiorCusto) {
                maiorCusto = custo;
                piorOpcao = v;
            }
            
            String tipo = v.getClass().getSimpleName();
            System.out.printf("  %-15s (%-12s) = R$ %.2f%n", 
                            v.getPlaca(), tipo, custo);
        }
        
        System.out.printf("%n✓ Melhor opção: %s (R$ %.2f)%n", 
                        melhorOpcao.getPlaca(), menorCusto);
        System.out.printf("✗ Pior opção: %s (R$ %.2f)%n", 
                        piorOpcao.getPlaca(), maiorCusto);
        System.out.printf("Custo total (todos): R$ %.2f%n", custoTotal);
    }
}