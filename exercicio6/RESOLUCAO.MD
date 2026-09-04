# Resolução: Sistema de Gestão de Fretes

## Passo 1: Criar a Enum StatusFrete

```java
package entities.enums;

public enum StatusFrete {
    PENDENTE,      // Aguardando despacho
    EM_TRANSITO,   // Em trânsito
    ENTREGUE,      // Entregue
    CANCELADO      // Cancelado
}
```

**Explicação:**
- Define os 4 estados possíveis de um frete
- `PENDENTE` = frete criado, aguarda saída
- `EM_TRANSITO` = saiu do depósito, está na estrada
- `ENTREGUE` = chegou ao destino
- `CANCELADO` = foi cancelado (não será mais feito)
- O compilador garante que nenhum outro status será aceito

---

## Passo 2: Criar a Enum TipoVeiculo

```java
package entities.enums;

public enum TipoVeiculo {
    CAMINHAO(0.00),  // Caminhão, sem desconto
    VAN(0.20),       // Van, 20% de desconto
    MOTO(0.40);      // Moto, 40% de desconto
    
    private final Double desconto;
    
    TipoVeiculo(Double desconto) {
        this.desconto = desconto;
    }
    
    public Double getDesconto() {
        return desconto;
    }
}
```

**Explicação linha por linha:**

| Linha | O que faz |
|-------|-----------|
| `CAMINHAO(0.00)` | Define tipo CAMINHAO com 0% de desconto |
| `VAN(0.20)` | Define tipo VAN com 20% de desconto |
| `MOTO(0.40)` | Define tipo MOTO com 40% de desconto |
| `private final Double desconto` | Atributo que armazena o desconto (imutável) |
| `TipoVeiculo(Double desconto)` | Construtor (chamado automaticamente ao instanciar enum) |
| `this.desconto = desconto` | Atribui o desconto recebido |
| `public Double getDesconto()` | Retorna o valor do desconto |

**Uso:**
```java
TipoVeiculo tipo = TipoVeiculo.CAMINHAO;
Double desconto = tipo.getDesconto();  // retorna 0.00
```

---

## Passo 3: Criar a Classe Rota

```java
package entities;

public class Rota {
    private String origem;
    private String destino;
    
    public Rota(String origem, String destino) {
        this.origem = origem;
        this.destino = destino;
    }
    
    public String getOrigem() {
        return origem;
    }
    
    public String getDestino() {
        return destino;
    }
    
    @Override
    public String toString() {
        return origem + " → " + destino;
    }
}
```

**Explicação linha por linha:**

| Linha | O que faz |
|-------|-----------|
| `private String origem` | Armazena a cidade de partida |
| `private String destino` | Armazena a cidade de chegada |
| `public Rota(String origem, String destino)` | Construtor que recebe origem e destino |
| `this.origem = origem` | Atribui a origem recebida ao atributo |
| `public String getOrigem()` | Retorna a origem |
| `public String getDestino()` | Retorna o destino |
| `@Override public String toString()` | Sobrescreve o método toString() |
| `return origem + " → " + destino` | Retorna a rota formatada (ex: "SP → RJ") |

**Uso:**
```java
Rota rota = new Rota("São Paulo", "Rio de Janeiro");
System.out.println(rota);  // São Paulo → Rio de Janeiro
```

---

## Passo 4: Criar a Classe Cliente

```java
package entities;

public class Cliente {
    private String nome;
    private String email;
    
    public Cliente(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    @Override
    public String toString() {
        return nome + " (" + email + ")";
    }
}
```

**Explicação linha por linha:**

| Linha | O que faz |
|-------|-----------|
| `private String nome` | Armazena o nome do cliente |
| `private String email` | Armazena o email do cliente |
| `public Cliente(String nome, String email)` | Construtor que recebe nome e email |
| `this.nome = nome` | Atribui o nome recebido |
| `this.email = email` | Atribui o email recebido |
| `public String getNome()` | Retorna o nome |
| `public String getEmail()` | Retorna o email |
| `return nome + " (" + email + ")"` | Formata a exibição (ex: "João Silva (joao@email.com)") |

---

## Passo 5: Criar a Classe Frete (Principal)

```java
package entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import entities.enums.StatusFrete;
import entities.enums.TipoVeiculo;

public class Frete {
    private Integer id;
    private Date data;
    private StatusFrete status;
    private TipoVeiculo tipoVeiculo;
    private Rota rota;
    private Cliente cliente;
    private Double distancia;
    private Double valorPorKm;
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static Integer proximoId = 1;
    
    public Frete(Date data, StatusFrete status, TipoVeiculo tipoVeiculo, 
                 Rota rota, Cliente cliente, Double distancia, Double valorPorKm) {
        this.id = proximoId++;
        this.data = data;
        this.status = status;
        this.tipoVeiculo = tipoVeiculo;
        this.rota = rota;
        this.cliente = cliente;
        this.distancia = distancia;
        this.valorPorKm = valorPorKm;
    }
    
    // Getters
    public Integer getId() {
        return id;
    }
    
    public Date getData() {
        return data;
    }
    
    public StatusFrete getStatus() {
        return status;
    }
    
    public void setStatus(StatusFrete status) {
        this.status = status;
    }
    
    public TipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }
    
    public Rota getRota() {
        return rota;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public Double getDistancia() {
        return distancia;
    }
    
    public Double getValorPorKm() {
        return valorPorKm;
    }
    
    // Cálculo de desconto
    public Double getDesconto() {
        return tipoVeiculo.getDiscount();
    }
    
    // Cálculo do valor total
    public Double valorTotal() {
        Double valorBase = distancia * valorPorKm;
        Double desconto = valorBase * getDesconto();
        return valorBase - desconto;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Frete #").append(id).append("\n");
        sb.append("  Data: ").append(sdf.format(data)).append("\n");
        sb.append("  Cliente: ").append(cliente).append("\n");
        sb.append("  Rota: ").append(rota).append("\n");
        sb.append("  Distância: ").append(String.format("%.0f", distancia)).append(" km\n");
        sb.append("  Tipo: ").append(tipoVeiculo).append("\n");
        sb.append("  Status: ").append(status).append("\n");
        sb.append("  Valor/km: R$ ").append(String.format("%.2f", valorPorKm)).append("\n");
        sb.append("  Desconto: ").append(String.format("%.0f", getDesconto() * 100)).append("%\n");
        sb.append("  Valor Total: R$ ").append(String.format("%.2f", valorTotal()));
        return sb.toString();
    }
}
```

**Explicação linha por linha:**

| Linha | O que faz |
|-------|-----------|
| `private Integer id` | Identificador único do frete |
| `private Date data` | Data de criação do frete |
| `private StatusFrete status` | Estado atual do frete (enum) |
| `private TipoVeiculo tipoVeiculo` | Tipo de veículo (enum com desconto) |
| `private Rota rota` | Composição: de onde para onde vai |
| `private Cliente cliente` | Composição: quem fez o frete |
| `private Double distancia` | Quilometragem |
| `private Double valorPorKm` | Valor cobrado por km |
| `private static SimpleDateFormat sdf` | Formata datas para "dd/MM/yyyy" |
| `private static Integer proximoId = 1` | Contador estático para auto-incrementar ID |
| `this.id = proximoId++` | Atribui ID e incrementa (1, 2, 3...) |
| `public void setStatus(StatusFrete status)` | Permite alterar o status do frete |
| `public Double getDesconto()` | Chama o método `getDiscount()` do enum |
| `public Double valorTotal()` | **CÁLCULO**: distância × valorPorKm × (1 - desconto) |
| `Double valorBase = distancia * valorPorKm` | Valor sem desconto |
| `Double desconto = valorBase * getDesconto()` | Valor do desconto em reais |
| `return valorBase - desconto` | Subtrai desconto do valor base |
| `sb.append(...)` | Constrói string de exibição linha por linha |
| `String.format("%.2f", ...)` | Formata números com 2 casas decimais |
| `String.format("%.0f", getDesconto() * 100)` | Formata desconto como percentual (0-100%) |

---

## Passo 6: Criar o Programa Principal

```java
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
        
        // Lista para armazenar todos os fretes
        List<Frete> fretes = new ArrayList<>();
        
        // 1. LER QUANTIDADE DE FRETES
        System.out.print("Quantos fretes deseja registrar? ");
        int n = sc.nextInt();
        sc.nextLine(); // limpar buffer do teclado
        
        // 2. LOOP PARA LER CADA FRETE
        for (int i = 1; i <= n; i++) {
            System.out.println("\n--- Frete #" + i + " ---");
            
            // 2.1 LER DADOS DO CLIENTE
            System.out.print("Nome do cliente: ");
            String nomeCliente = sc.nextLine();
            
            System.out.print("Email do cliente: ");
            String emailCliente = sc.nextLine();
            
            Cliente cliente = new Cliente(nomeCliente, emailCliente);
            
            // 2.2 LER DADOS DA ROTA
            System.out.print("Cidade de origem: ");
            String origem = sc.nextLine();
            
            System.out.print("Cidade de destino: ");
            String destino = sc.nextLine();
            
            Rota rota = new Rota(origem, destino);
            
            // 2.3 LER DISTÂNCIA
            System.out.print("Distância (km): ");
            Double distancia = sc.nextDouble();
            
            // 2.4 LER VALOR POR KM
            System.out.print("Valor por km (R$): ");
            Double valorPorKm = sc.nextDouble();
            sc.nextLine(); // limpar buffer
            
            // 2.5 LER TIPO DE VEÍCULO
            System.out.print("Tipo de veículo (CAMINHAO/VAN/MOTO): ");
            String tipoStr = sc.nextLine().toUpperCase();
            TipoVeiculo tipo = TipoVeiculo.valueOf(tipoStr);
            
            // 2.6 LER STATUS
            System.out.print("Status (PENDENTE/EM_TRANSITO/ENTREGUE/CANCELADO): ");
            String statusStr = sc.nextLine().toUpperCase();
            StatusFrete status = StatusFrete.valueOf(statusStr);
            
            // 2.7 CRIAR O FRETE
            Frete frete = new Frete(new Date(), status, tipo, rota, cliente, distancia, valorPorKm);
            
            // 2.8 ADICIONAR À LISTA
            fretes.add(frete);
        }
        
        // 3. EXIBIR TODOS OS FRETES
        System.out.println("\n========== FRETES REGISTRADOS ==========");
        for (Frete frete : fretes) {
            System.out.println(frete);
            System.out.println();
        }
        
        // 4. EXIBIR RELATÓRIO POR STATUS
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
```

**Explicação linha por linha:**

| Linha | O que faz |
|-------|-----------|
| `List<Frete> fretes = new ArrayList<>()` | Cria lista vazia para armazenar fretes |
| `System.out.print("Quantos fretes...")` | Pergunta quantos fretes serão criados |
| `int n = sc.nextInt()` | Lê a quantidade como inteiro |
| `sc.nextLine()` | Limpa o "\n" deixado pelo nextInt() |
| `for (int i = 1; i <= n; i++)` | Loop que se repete N vezes |
| `String nomeCliente = sc.nextLine()` | Lê nome completo (com espaços) |
| `Cliente cliente = new Cliente(...)` | Cria instância do cliente |
| `Rota rota = new Rota(origem, destino)` | Cria rota com origem e destino |
| `sc.nextDouble()` | Lê um número decimal (distância, valor/km) |
| `String tipoStr = sc.nextLine().toUpperCase()` | Lê tipo e converte para maiúscula |
| `TipoVeiculo tipo = TipoVeiculo.valueOf(tipoStr)` | Converte string em enum |
| `Frete frete = new Frete(...)` | Cria frete com todos os dados |
| `fretes.add(frete)` | Adiciona frete à lista |
| `for (Frete frete : fretes)` | Loop para cada frete criado |
| `System.out.println(frete)` | Chama `toString()` do frete |
| `for (StatusFrete statusAtual : StatusFrete.values())` | Loop por cada status (PENDING, IN_TRANSIT, DELIVERED, CANCELLED) |
| `if (frete.getStatus() == statusAtual)` | Verifica se o frete tem este status |
| `quantidade++` | Incrementa contador de fretes deste status |
| `total += frete.valorTotal()` | Soma o valor do frete ao total |
| `System.out.printf(...)` | Imprime formatado com 2 casas decimais |
| `sc.close()` | Fecha o Scanner (libera recursos) |

---

## 🎯 Fluxo de Execução Completo

```
1. Programa inicia
   ↓
2. Pergunta: "Quantos fretes?"
   ↓ (usuário digita 3)
3. LOOP 1: Lê dados do cliente, rota, distância, tipo, status
   ↓
   Cria: Frete #1 (ID auto-incrementado)
   ↓
4. LOOP 2: Lê dados do segundo frete
   ↓
   Cria: Frete #2
   ↓
5. LOOP 3: Lê dados do terceiro frete
   ↓
   Cria: Frete #3
   ↓
6. Exibe todos os 3 fretes com toString()
   ↓
7. Calcula totais por status:
   - PENDING: quantidade e valor
   - IN_TRANSIT: quantidade e valor
   - DELIVERED: quantidade e valor
   - CANCELLED: quantidade e valor
   ↓
8. Programa encerra
```

---

## 📝 Exemplo de Entrada e Saída

### INPUT:
```
Quantos fretes deseja registrar? 3

--- Frete #1 ---
Nome do cliente: João Silva
Email do cliente: joao@email.com
Cidade de origem: São Paulo
Cidade de destino: Rio de Janeiro
Distância (km): 400
Valor por km (R$): 5.00
Tipo de veículo (CAMINHAO/VAN/MOTO): CAMINHAO
Status (PENDENTE/EM_TRANSITO/ENTREGUE/CANCELADO): PENDENTE

--- Frete #2 ---
Nome do cliente: Maria Santos
Email do cliente: maria@email.com
Cidade de origem: Curitiba
Cidade de destino: São Paulo
Distância (km): 400
Valor por km (R$): 5.00
Tipo de veículo (CAMINHAO/VAN/MOTO): MOTO
Status (PENDENTE/EM_TRANSITO/ENTREGUE/CANCELADO): EM_TRANSITO

--- Frete #3 ---
Nome do cliente: Pedro Costa
Email do cliente: pedro@email.com
Cidade de origem: Belo Horizonte
Cidade de destino: Brasília
Distância (km): 700
Valor por km (R$): 5.00
Tipo de veículo (CAMINHAO/VAN/MOTO): VAN
Status (PENDENTE/EM_TRANSITO/ENTREGUE/CANCELADO): ENTREGUE
```

### OUTPUT:
```
========== FRETES REGISTRADOS ==========
Frete #1
  Data: 04/09/2026
  Cliente: João Silva (joao@email.com)
  Rota: São Paulo → Rio de Janeiro
  Distância: 400 km
  Tipo: CAMINHAO
  Status: PENDENTE
  Valor/km: R$ 5.00
  Desconto: 0%
  Valor Total: R$ 2000.00

Frete #2
  Data: 04/09/2026
  Cliente: Maria Santos (maria@email.com)
  Rota: Curitiba → São Paulo
  Distância: 400 km
  Tipo: MOTO
  Status: EM_TRANSITO
  Valor/km: R$ 5.00
  Desconto: 40%
  Valor Total: R$ 1200.00

Frete #3
  Data: 04/09/2026
  Cliente: Pedro Costa (pedro@email.com)
  Rota: Belo Horizonte → Brasília
  Distância: 700 km
  Tipo: VAN
  Status: ENTREGUE
  Valor/km: R$ 5.00
  Desconto: 20%
  Valor Total: R$ 2800.00

========== RELATÓRIO POR STATUS ==========
PENDENTE: 1 frete(s) - Total: R$ 2000.00
EM_TRANSITO: 1 frete(s) - Total: R$ 1200.00
ENTREGUE: 1 frete(s) - Total: R$ 2800.00
CANCELADO: 0 frete(s) - Total: R$ 0.00
```

---

