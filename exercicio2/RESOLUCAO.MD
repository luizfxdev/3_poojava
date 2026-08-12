## O que foi feito?
 
Criado um **programa que simula uma conta bancária** com operações de depósito, saque e gerenciamento de informações. É um exemplo prático de **Programação Orientada a Objetos (POO)**.
 
---
 
## Conceitos de POO Utilizados
 
### 1️⃣ **Classe e Atributos**
A classe `ContaBancaria` representa uma conta real com suas características:
 
```java
public class ContaBancaria {
    private final int numero;           // Número da conta (não muda)
    private String titular;             // Nome do dono (pode mudar)
    private double saldo;               // Valor da moeda
    private static final double TAXA_SAQUE = 5.00;  // Taxa fixa
}
```
 
**O que significa:**
- `private` = Protege os dados (não podem ser acessados diretamente lá de fora)
- `final` = Uma vez criado, não pode ser modificado (como o número da conta)
- `static` = Compartilhado por TODAS as contas (mesma taxa para todos)
---
 
### 2️⃣ **Construtores (Sobrecarga)**
 
Criamos **dois construtores** para flexibilidade:
 
```java
// Construtor 1: Com depósito inicial
public ContaBancaria(int numero, String titular, double depositoInicial) {
    this.numero = numero;
    this.titular = titular;
    this.saldo = depositoInicial;
}
 
// Construtor 2: Sem depósito inicial (sobrecarga)
public ContaBancaria(int numero, String titular) {
    this(numero, titular, 0.0);  // Chama o outro construtor
}
```
 
**Por que dois construtores?**
- Construtor 1: Quando a pessoa já tem dinheiro para depositar
- Construtor 2: Quando a pessoa quer abrir com saldo zero
- `this()` = Evita repetir código, chama o outro construtor
**Uso:**
```java
ContaBancaria conta1 = new ContaBancaria(1234, "João", 1000.00);  // Com dinheiro
ContaBancaria conta2 = new ContaBancaria(5678, "Maria");           // Sem dinheiro
```
 
---
 
### 3️⃣ **Encapsulamento**
 
Protegemos os dados com `private` e criamos getters:
 
```java
// Getters: apenas leitura, sem setters (proteção total)
public int getNumero() {
    return numero;
}
 
public String getTitular() {
    return titular;
}
 
public double getSaldo() {
    return saldo;
}
```
 
**Por que não tem setter para saldo?**
- O saldo **só pode mudar por depósito ou saque**, nunca diretamente
- Isso garante segurança: ninguém consegue fazer `conta.setSaldo(1000000)`
- Só os métodos `depositar()` e `sacar()` modificam o saldo
---
 
### 4️⃣ **Métodos com Lógica**
 
#### **Depósito:**
```java
public void depositar(double valor) {
    if (valor > 0) {
        saldo += valor;
        System.out.printf("Depósito de R$ %.2f realizado com sucesso%n", valor);
    } else {
        System.out.println("Valor de depósito deve ser positivo");
    }
}
```
 
**O que acontece:**
1. Verifica se o valor é positivo
2. Se sim, **adiciona ao saldo**
3. Se não, mostra erro
#### **Saque:**
```java
public void sacar(double valor) {
    if (valor > 0) {
        double total = valor + TAXA_SAQUE;  // Saque + taxa de R$ 5
        saldo -= total;
        System.out.printf("Saque de R$ %.2f realizado (taxa R$ %.2f)%n", valor, TAXA_SAQUE);
    } else {
        System.out.println("Valor de saque deve ser positivo");
    }
}
```
 
**O que acontece:**
1. Verifica se o valor é positivo
2. Calcula o total (valor do saque + R$ 5 de taxa)
3. **Deduz do saldo** (mesmo que fique negativo)
4. Exibe mensagem de confirmação
#### **Alterar Titular:**
```java
public void alterarTitular(String novoNome) {
    this.titular = novoNome;
}
```
 
**Simples:** Apenas muda o nome (permitido conforme requisito)
 
#### **Exibir Dados:**
```java
public void exibir() {
    System.out.println("=== Dados da Conta ===");
    System.out.printf("Número: %d%n", numero);
    System.out.printf("Titular: %s%n", titular);
    System.out.printf("Saldo: R$ %.2f%n", saldo);
    System.out.println();
}
```
 
**Mostra todas as informações da conta formatadas**
 
---
 
## Exemplo Prático Passo a Passo
 
```java
public class Main {
    public static void main(String[] args) {
        // Passo 1: Criar uma conta com R$ 1000
        ContaBancaria conta = new ContaBancaria(1234, "João Silva", 1000.00);
        conta.exibir();
        // Resultado:
        // === Dados da Conta ===
        // Número: 1234
        // Titular: João Silva
        // Saldo: R$ 1000.00
 
        // Passo 2: Depositar R$ 500
        conta.depositar(500.00);
        // Resultado: "Depósito de R$ 500.00 realizado com sucesso"
        // Saldo agora: R$ 1500.00
 
        // Passo 3: Sacar R$ 200
        conta.sacar(200.00);
        // Resultado: "Saque de R$ 200.00 realizado (taxa R$ 5.00)"
        // Saldo agora: R$ 1295.00 (1500 - 200 - 5)
 
        // Passo 4: Exibir dados finais
        conta.exibir();
    }
}
```
 
---
 
## Resumo das Decisões Importantes
 
| Decisão | Por quê? |
|---------|----------|
| **Número com `final`** | Não pode mudar após criação (regra do banco) |
| **Saldo com `private`** | Ninguém acessa diretamente, só por métodos seguros |
| **Dois construtores** | Flexibilidade: com ou sem depósito inicial |
| **Taxa automática** | Deduz R$ 5 junto com o saque, sem necessidade de calcular separadamente |
| **Saldo pode ser negativo** | Requisito: se não houver dinheiro, pode ficar no vermelho |
| **`printf("%.2f")`** | Mostra valores monetários com 2 casas decimais (padrão BRL) |
 
---
 
## Conceitos-Chave para Fixação
 
✅ **Classe** = Template/modelo de um objeto real (a conta)
 
✅ **Atributos** = Características da classe (número, titular, saldo)
 
✅ **Métodos** = Ações que a classe pode fazer (depositar, sacar, exibir)
 
✅ **Encapsulamento** = Proteger dados com `private` e `public`
 
✅ **Construtores** = Inicializar um objeto novo
 
✅ **Sobrecarga** = Múltiplas versões do mesmo método (dois construtores)
 
✅ **This** = Referência ao próprio objeto (`this.saldo`)
 
✅ **Static** = Compartilhado por todas as instâncias (taxa é única para todas as contas)
 
---
 
## Analogia com League of Legends 🎮
 
Pense na classe como um **campeão**:
- **Atributos** = Stats (vida, ataque, defesa)
- **Métodos** = Habilidades (Q, W, E, R)
- **Encapsulamento** = Você não pode mudar a vida dele diretamente; só pode atacar ou curar
- **Construtores** = Diferentes campeões (tons de pele diferentes, skins) mas mesma essência
- **Sobrecarga** = Mesma habilidade, diferentes formas de usar
---