import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import entities.Cliente;
import entities.Frete;
import entities.Rota;
import entities.enums.StatusFrete;
import entities.enums.TipoVeiculo;

public class Program {
    public static void main(String[] args) throws ParseException {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        
        List<Frete> fretes = new ArrayList<>();
        
        
        System.out.print("Quantos fretes deseja registrar? ");
        int n = sc.nextInt();
        sc.nextLine(); 
        
        
        for (int i = 1; i <= n; i++) {
            System.out.println("\n--- Frete #" + i + " ---");
            
            
            System.out.print("Nome do cliente: ");
            String nomeCliente = sc.nextLine();
            
            System.out.print("Email do cliente: ");
            String emailCliente = sc.nextLine();
            
            Cliente cliente = new Cliente(nomeCliente, emailCliente);
            
            
            System.out.print("Cidade de origem: ");
            String origem = sc.nextLine();
            
            System.out.print("Cidade de destino: ");
            String destino = sc.nextLine();
            
            Rota rota = new Rota(origem, destino);
            
            
            System.out.print("Distância (km): ");
            Double distancia = sc.nextDouble();
            
            
            System.out.print("Valor por km (R$): ");
            Double valorPorKm = sc.nextDouble();
            sc.nextLine(); 
            
            
            System.out.print("Tipo de veículo (CAMINHAO/VAN/MOTO): ");
            String tipoStr = sc.nextLine().toUpperCase();
            TipoVeiculo tipo = TipoVeiculo.valueOf(tipoStr);
            
            
            System.out.print("Status (PENDENTE/EM_TRANSITO/ENTREGUE/CANCELADO): ");
            String statusStr = sc.nextLine().toUpperCase();
            StatusFrete status = StatusFrete.valueOf(statusStr);
            
            
            Frete frete = new Frete(new Date(), status, tipo, rota, cliente, distancia, valorPorKm);
            
            
            fretes.add(frete);
        }
        
        
        System.out.println("\n========== FRETES REGISTRADOS ==========");
        for (Frete frete : fretes) {
            System.out.println(frete);
            System.out.println();
        }
        
        
        System.out.println("\n========== RELATÓRIO POR STATUS ==========");
        
        for (StatusFrete statusAtual : StatusFrete.values()) {
            Double total = 0.0;
            int quantidade = 0;
            
            for (Frete frete : fretes) {
                if (frete.getStatus() == statusAtual) {
                    quantidade++;
                    total += frete.valorTotal();
                }
            }
            
            System.out.printf("%s: %d frete(s) - Total: R$ %.2f%n", 
                            statusAtual, quantidade, total);
        }
        
        sc.close();
    }
}