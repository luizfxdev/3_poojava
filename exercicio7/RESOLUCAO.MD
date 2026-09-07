# Exercício Resolvido: Sistema de Logística com Herança e Polimorfismo
 
## 1. Visão Geral do Problema
 
Criar um sistema de logística que calcule custos de entrega para diferentes tipos de veículos (Caminhão, Motocicleta e Van), onde cada veículo tem uma **estratégia diferente** de cálculo de custo baseada em suas características específicas.
 
**Requisitos:**
- Classe abstrata `Veiculo` que define o contrato comum
- Subclasses concretas: `Caminhao`, `Motocicleta`, `Van`
- Cada veículo calcula custo diferente (polimorfismo)
- Sistema que processa lista de entregas
---
 
## 2. Estrutura de Classes (Arquitetura)
 
```
         Veiculo (abstrata)
            ↑  ↑  ↑
            |  |  |
       Cam  |  |  Van
       inhão|  |
            | Moto
            | cicleta
```
 
---

## 3. Onde Aplicamos HERANÇA
 
### O Que É Herança?
Uma classe herda atributos e métodos de outra classe usando `extends`.
 
### Aplicação no Projeto
 
```java
public abstract class Veiculo {
    protected String placa;
    protected double capacidadeKg;
    protected double velocidadeKmh;
    protected double custoPorKm;
}
 
// Caminhão HERDA de Veiculo
public class Caminhao extends Veiculo {
    private int eixos;
    private double cargaAtual;
    
    // Herda: placa, capacidadeKg, velocidadeKmh, custoPorKm
    // Adiciona: eixos, cargaAtual
}
```
 
### Por Que Usamos Herança?
 
| Sem Herança (RUIM) | Com Herança (BOM) |
|--------------------|-------------------|
| Cada veículo repete `placa`, `capacidadeKg`, `velocidadeKmh` | Código comum na superclasse |
| Modificar um atributo = alterar 3 classes | Modificar uma vez |
| Código duplicado = erro fácil | Código centralizado |
 
---
 
## 4. Onde Aplicamos POLIMORFISMO
 
### O Que É Polimorfismo?
Uma variável de tipo genérico (superclasse) aponta para objetos de tipos específicos (subclasses), cada um com comportamento diferente.
 
### Aplicação no Projeto
 
```java
// Sem polimorfismo (RUIM):
if (veiculo instanceof Caminhao) {
    Caminhao c = (Caminhao) veiculo;
    custo = c.calcularCusto(100);
} else if (veiculo instanceof Motocicleta) {
    Motocicleta m = (Motocicleta) veiculo;
    custo = m.calcularCusto(100);
} else if (veiculo instanceof Van) {
    Van v = (Van) veiculo;
    custo = v.calcularCusto(100);
}
 
// Com polimorfismo (BOM):
List<Veiculo> frota = new ArrayList<>();
frota.add(new Caminhao(...));
frota.add(new Motocicleta(...));
frota.add(new Van(...));
 
for (Veiculo v : frota) {
    double custo = v.calcularCusto(100);  // ← POLIMORFISMO!
    System.out.println(v.getPlaca() + " custa R$ " + custo);
}
```
 
### Cada Veículo Calcula Diferente
 
```java
// Caminhão
@Override
public double calcularCusto(double distancia) {
    double custoBase = custoPorKm * distancia;
    double custoTonelada = (cargaAtual / 1000.0) * 50.0;
    double custoEixo = eixos * 30.0;
    return custoBase + custoTonelada + custoEixo;
}
 
// Motocicleta
@Override
public double calcularCusto(double distancia) {
    return custoPorKm * distancia;  // Sem taxa extra!
}
 
// Van
@Override
public double calcularCusto(double distancia) {
    double custoBase = custoPorKm * distancia;
    double litros = distancia * consumoLitroKm;
    double custoCombustivel = litros * precoGasolinaPorLitro;
    return custoBase + custoCombustivel;
}
```
 
### A Magia do Polimorfismo
 
Quando executa `v.calcularCusto(100)`:
 
```
List[0] = Caminhao   → executa calcularCusto() do Caminhao
List[1] = Motocicleta → executa calcularCusto() da Motocicleta
List[2] = Van        → executa calcularCusto() da Van
```
 
**Java automaticamente escolhe a versão correta!**
 
---
 
## 5. Classe Abstrata: O Contrato
 
```java
public abstract class Veiculo {
    // Método abstrato: OBRIGA subclasses a implementar
    public abstract double calcularCusto(double distancia);
    
    // Método concreto: compartilhado por todos
    public double calcularTempoEstimado(double distancia) {
        return distancia / velocidadeKmh;
    }
}
```
 
### Por Que Abstract?
 
| Sem Abstract | Com Abstract |
|--------------|--------------|
| `new Veiculo()` é permitido (ambíguo!) | Compile ERROR (correto!) |
| Subclasses podem não implementar `calcularCusto()` | Compile ERROR se não implementar |
| Nenhuma garantia de comportamento | Contrato garantido |
 
---
 
## 6. Exemplo Prático: Comparar Custos
 
**Entrada:** Entrega de 100 km
 
**Código:**
```java
List<Veiculo> frota = new ArrayList<>();
frota.add(new Caminhao("ABC-1234", 5000, 80, 5.0, 3));
frota.add(new Motocicleta("XYZ-5678", 50, 120, 3.0, 600, 150));
frota.add(new Van("VAN-9999", 1500, 100, 4.0, 5, 0.08, 6.50));
 
double distancia = 100;
 
for (Veiculo v : frota) {
    System.out.printf("%s: R$ %.2f%n", 
        v.getPlaca(), 
        v.calcularCusto(distancia));
}
```
 
**Saída:**
```
ABC-1234: R$ 680.00  (Caminhão: base + taxa/tonelada + taxa/eixo)
XYZ-5678: R$ 300.00  (Motocicleta: apenas base)
VAN-9999: R$ 452.00  (Van: base + combustível)
```
 
**Sem fazer `if/else`!** Polimorfismo resolveu.
 
---
 
## 7. Comparação Lado a Lado
 
### Herança vs Polimorfismo
 
| Aspecto | Herança | Polimorfismo |
|---------|---------|--------------|
| **O Quê** | Compartilhar código comum | Executar código diferente por tipo |
| **Como** | `extends` | `@Override` + Lista de superclasse |
| **Benefício** | Evita repetição | Evita `if/else` |
| **Exemplo** | Todos têm `placa`, `velocidade` | Cada um calcula custo diferente |
 
### Sem Herança e Polimorfismo (RUIM)
 
```java
class CaminhaoA {
    String placa;
    double capacidadeKg;
    double velocidadeKmh;
    double custoPorKm;
    int eixos;
    
    double calcularCusto(double dist) { ... }
}
 
class MotocicletaA {
    String placa;           // REPETIÇÃO!
    double capacidadeKg;    // REPETIÇÃO!
    double velocidadeKmh;   // REPETIÇÃO!
    double custoPorKm;      // REPETIÇÃO!
    int cilindrada;
    
    double calcularCusto(double dist) { ... }
}
 
class VanA {
    String placa;           // REPETIÇÃO!
    double capacidadeKg;    // REPETIÇÃO!
    double velocidadeKmh;   // REPETIÇÃO!
    double custoPorKm;      // REPETIÇÃO!
    double consumoLitroKm;
    
    double calcularCusto(double dist) { ... }
}
 
// Para calcular custo, precisa:
if (veiculo instanceof CaminhaoA) { ... }
else if (veiculo instanceof MotocicletaA) { ... }
else if (veiculo instanceof VanA) { ... }
```
 
**Problemas:**
- ❌ Código duplicado (4 atributos repetidos 3 vezes)
- ❌ Difícil de manter (modificar nome do atributo = 3 lugares)
- ❌ Muitos `if/else` (frágil, propenso a erro)
- ❌ Difícil adicionar novo tipo (cria mais repetição)
### Com Herança e Polimorfismo (BOM)
 
```java
abstract class Veiculo {
    protected String placa;
    protected double capacidadeKg;
    protected double velocidadeKmh;
    protected double custoPorKm;
    
    abstract double calcularCusto(double dist);
}
 
class Caminhao extends Veiculo {
    int eixos;
    
    @Override
    double calcularCusto(double dist) { ... }
}
 
class Motocicleta extends Veiculo {
    int cilindrada;
    
    @Override
    double calcularCusto(double dist) { ... }
}
 
class Van extends Veiculo {
    double consumoLitroKm;
    
    @Override
    double calcularCusto(double dist) { ... }
}
 
// Para calcular custo:
List<Veiculo> frota = new ArrayList<>();
frota.add(new Caminhao(...));
frota.add(new Motocicleta(...));
frota.add(new Van(...));
 
for (Veiculo v : frota) {
    double custo = v.calcularCusto(100);  // Polimorfismo!
}
```
 
**Vantagens:**
- ✅ Código comum centralizado (reuso)
- ✅ Fácil de manter (modificar uma vez)
- ✅ Nenhum `if/else` (polimorfismo resolve)
- ✅ Fácil adicionar novo tipo (herda de Veiculo)
---
 
## 8. Cenários Onde Aplicamos
 
### Cenário 1: Calcular Custo de Entrega
```
Usuário digita: distância = 100 km
Sistema itera sobre a frota (Caminhão, Moto, Van)
Cada um calcula seu próprio custo (polimorfismo)
Resultado: 3 valores diferentes, 1 loop
```
 
### Cenário 2: Listar Frota
```
Usuário escolhe listar
Sistema itera sobre List<Veiculo>
Cada um imprime seus dados via toString() (polimorfismo)
Resultado: Caminhão mostra eixos, Moto mostra cilindrada, Van mostra consumo
```
 
### Cenário 3: Adicionar Novo Veículo
```
Sem herança/polimorfismo: modificar código existente (risco!)
Com herança/polimorfismo: criar classe Bicicleta extends Veiculo
Pronto! Sistema funciona automaticamente com novo tipo.
```
 
---
 
## 9. Palavras-Chave Usadas
 
| Palavra-Chave | Significado | Uso |
|---|---|---|
| `abstract` | Classe/método sem implementação | Define contrato, força subclasses a implementar |
| `extends` | Herança | Caminhao extends Veiculo |
| `@Override` | Sobrescrita | Cada subclasse implementa calcularCusto() |
| `protected` | Modificador de acesso | Subclasses acessam atributos da superclasse |
| `List<Veiculo>` | Polimorfismo | Uma lista contém 3 tipos diferentes |
 
---
 
## 10. Resumo Final
 
**Herança:** Caminhão, Motocicleta e Van compartilham código comum (placa, velocidade, etc.) herdando de Veiculo.
 
**Polimorfismo:** Cada um tem seu próprio `calcularCusto()`. Quando chamamos `v.calcularCusto()` em uma List, Java automaticamente escolhe a versão correta.
 
**Resultado:** Código limpo, sem repetição, sem `if/else`, e fácil de expandir com novos veículos.
 
**Aplicação:** Sistema real de logística onde diferentes transportes têm custo diferente.
 
---
 
## ✅ Conceitos Aprendidos
 
- ✅ Herança reduz código duplicado
- ✅ Classes abstratas definem contrato
- ✅ Métodos abstratos obrigam implementação
- ✅ Polimorfismo evita `if/else`
- ✅ `@Override` deixa claro que estamos sobrescrevendo
- ✅ `protected` permite acesso de subclasses
- ✅ Uma List pode conter tipos diferentes (polimorfismo)