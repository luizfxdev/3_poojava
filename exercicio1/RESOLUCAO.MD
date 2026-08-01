# Explicação do Programa: Comparação de Área de Triângulos

## ?? Objetivo do Exercício

Ler as medidas dos lados de dois triângulos (X e Y) e determinar qual triângulo possui a maior área, usando a **fórmula de Heron** para o cálculo.

---

## ?? Fórmula de Heron

A **fórmula de Heron** permite calcular a área de um triângulo conhecendo apenas os comprimentos dos três lados (a, b, c).

**Fórmula:**
```
p = (a + b + c) / 2

área = ?[p(p-a)(p-b)(p-c)]
```

Onde:
- **a, b, c**: comprimentos dos três lados
- **p**: semi-perímetro (metade do perímetro)
- **?**: raiz quadrada

**Exemplo (Triângulo X: 3, 4, 5):**
```
p = (3 + 4 + 5) / 2 = 12 / 2 = 6
área = ?[6 × (6-3) × (6-4) × (6-5)]
área = ?[6 × 3 × 2 × 1]
área = ?36 = 6.0000
```

---

## ??? Arquitetura do Programa

O programa usa **Programação Orientada a Objetos (POO)** com duas classes:

### 1?? Classe `Triangle`

Encapsula os dados e comportamento de um triângulo.

### 2?? Classe `Program`

Implementa a lógica principal (entrada, processamento, comparação).

---

## ?? Classe Triangle

### Declaração

```java
public class Triangle {
```

Define uma classe pública chamada `Triangle` que pode ser acessada de qualquer lugar do projeto.

---

### Atributos (Variáveis de Instância)

```java
private double a;
private double b;
private double c;
```

| Atributo | Tipo | Descrição | Modificador |
|----------|------|-----------|------------|
| `a` | `double` | Primeiro lado | `private` |
| `b` | `double` | Segundo lado | `private` |
| `c` | `double` | Terceiro lado | `private` |

**Modificador `private`:** Os atributos são acessíveis apenas dentro da classe, garantindo **encapsulamento**.

---

### Construtor

```java
public Triangle(double a, double b, double c) {
    this.a = a;
    this.b = b;
    this.c = c;
}
```

**O que é um construtor?**

Um **construtor** é um método especial que:
- Tem o **mesmo nome da classe** (`Triangle`)
- É executado **quando o objeto é criado**
- Inicializa os atributos do objeto

**Sintaxe:**
```java
new Triangle(3.00, 4.00, 5.00)
```

Quando você escreve `new Triangle(...)`, o construtor é chamado automaticamente.

**Palavra-chave `this`:**

```java
this.a = a;
```

- `this.a`: refere-se ao **atributo da classe**
- `a`: refere-se ao **parâmetro do construtor**
- `this.a = a`: "atribua o valor do parâmetro ao atributo"

**Exemplo:**
```java
Triangle x = new Triangle(3.0, 4.0, 5.0);
// this.a recebe 3.0
// this.b recebe 4.0
// this.c recebe 5.0
```

---

### Método: `area()`

```java
public double area() {
    double p = (a + b + c) / 2.0;
    double area = Math.sqrt(p * (p - a) * (p - b) * (p - c));
    return area;
}
```

#### Análise linha por linha

**`public double area()`**
- `public`: método acessível de fora da classe
- `double`: retorna um valor decimal
- `area()`: nome do método

**`double p = (a + b + c) / 2.0;`**
- Calcula o semi-perímetro `p`
- Usa `2.0` (ponto flutuante) para garantir divisão decimal

**Exemplo (Triângulo 3, 4, 5):**
```
p = (3 + 4 + 5) / 2.0
p = 12 / 2.0
p = 6.0
```

**`double area = Math.sqrt(p * (p - a) * (p - b) * (p - c));`**
- `Math.sqrt()`: calcula a raiz quadrada
- Multiplica os termos: `p × (p-a) × (p-b) × (p-c)`

**Exemplo (continuação):**
```
(p - a) = 6 - 3 = 3
(p - b) = 6 - 4 = 2
(p - c) = 6 - 5 = 1

p × (p-a) × (p-b) × (p-c) = 6 × 3 × 2 × 1 = 36
Math.sqrt(36) = 6.0
```

**`return area;`**
- Retorna o valor calculado para quem chamou o método

---

### Getters (Métodos Acessadores)

```java
public double getA() {
    return a;
}

public double getB() {
    return b;
}

public double getC() {
    return c;
}
```

**Por que getters?**

Como os atributos são `private`, não podem ser acessados diretamente de fora da classe.

```java
// ? ERRADO: não compila
double lado = x.a;

// ? CORRETO: usa getter
double lado = x.getA();
```

**Padrão de nomenclatura:**
- Getters começam com `get`
- Seguido do nome do atributo (primeira letra maiúscula)
- `getA()`, `getB()`, `getC()`

---

## ?? Classe Program

### Declaração e Main

```java
public class Program {
    
    public static void main(String[] args) {
```

**`public static void main(String[] args)`**
- `public`: acessível de qualquer lugar
- `static`: pertence à classe (não a objetos)
- `void`: não retorna valor
- `String[] args`: parâmetro de linha de comando

Este é o **ponto de entrada** do programa (quando você executa: `java Program`).

---

### Try-with-resources

```java
try (Scanner sc = new Scanner(System.in)) {
    // código aqui
}
```

Garante que o Scanner seja **fechado automaticamente** ao sair do bloco (boa prática).

---

### Leitura de Entrada (Triângulo X)

```java
System.out.println("Enter the measures of triangle X:");
double a1 = sc.nextDouble();
double b1 = sc.nextDouble();
double c1 = sc.nextDouble();
```

**Fluxo:**
1. Imprime mensagem
2. Lê três valores decimais do teclado
3. Armazena em `a1`, `b1`, `c1`

**Exemplo de entrada:**
```
Enter the measures of triangle X:
3.00
4.00
5.00
```

---

### Criação do Objeto (Triângulo X)

```java
Triangle x = new Triangle(a1, b1, c1);
```

**Processo:**
1. Palavra-chave `new`: cria um novo objeto
2. `Triangle(a1, b1, c1)`: chama o construtor
3. Os valores são passados aos atributos
4. `x`: variável que referencia o objeto criado

---

### Leitura de Entrada (Triângulo Y)

```java
System.out.println("Enter the measures of triangle Y:");
double a2 = sc.nextDouble();
double b2 = sc.nextDouble();
double c2 = sc.nextDouble();

Triangle y = new Triangle(a2, b2, c2);
```

Mesmo processo que para o triângulo X.

---

### Cálculo das Áreas

```java
double areaX = x.area();
double areaY = y.area();
```

**Processo:**
1. Chama o método `area()` do objeto `x`
2. Calcula usando a fórmula de Heron
3. Retorna o valor
4. Armazena em `areaX`
5. Repete para o objeto `y`

---

### Impressão das Áreas

```java
System.out.printf("Triangle X area: %.4f%n", areaX);
System.out.printf("Triangle Y area: %.4f%n", areaY);
```

**`printf` (print formatted)**
- `%.4f`: formata com **4 casas decimais**
- `%n`: quebra de linha

**Exemplo de saída:**
```
Triangle X area: 6.0000
Triangle Y area: 7.5638
```

---

### Comparação e Resultado

```java
if (areaX > areaY) {
    System.out.println("Larger area: X");
}
else if (areaY > areaX) {
    System.out.println("Larger area: Y");
}
else {
    System.out.println("Larger area: Equal");
}
```

**Lógica:**
1. Se área X > área Y ? imprime "X"
2. Senão se área Y > área X ? imprime "Y"
3. Senão (iguais) ? imprime "Equal"

---

## ?? Fluxo de Execução Completo

```
????????????????????????????????
? java Program                 ?
????????????????????????????????
             ?
????????????????????????????????
? Imprime: Enter measures of   ?
? triangle X:                  ?
????????????????????????????????
             ?
????????????????????????????????
? Lê: a1=3.00, b1=4.00, c1=5.00?
????????????????????????????????
             ?
????????????????????????????????
? Cria: Triangle x =           ?
? new Triangle(3, 4, 5)        ?
????????????????????????????????
             ?
????????????????????????????????
? Imprime: Enter measures of   ?
? triangle Y:                  ?
????????????????????????????????
             ?
????????????????????????????????
? Lê: a2=7.50, b2=4.50, c2=4.50?
????????????????????????????????
             ?
????????????????????????????????
? Cria: Triangle y =           ?
? new Triangle(7.5, 4.5, 4.5)  ?
????????????????????????????????
             ?
????????????????????????????????
? Calcula: areaX = x.area()    ?
? areaX = 6.0000               ?
????????????????????????????????
             ?
????????????????????????????????
? Calcula: areaY = y.area()    ?
? areaY = 7.5638               ?
????????????????????????????????
             ?
????????????????????????????????
? Imprime: Triangle X area:    ?
? 6.0000                       ?
????????????????????????????????
             ?
????????????????????????????????
? Imprime: Triangle Y area:    ?
? 7.5638                       ?
????????????????????????????????
             ?
????????????????????????????????
? Compara: 7.5638 > 6.0000?    ?
? SIM                          ?
????????????????????????????????
             ?
????????????????????????????????
? Imprime: Larger area: Y      ?
????????????????????????????????
             ?
????????????????????????????????
? Scanner fecha automaticamente ?
????????????????????????????????
```

---

## ?? Exemplo de Execução

### Entrada

```
Enter the measures of triangle X:
3.00
4.00
5.00
Enter the measures of triangle Y:
7.50
4.50
4.50
```

### Saída

```
Triangle X area: 6.0000
Triangle Y area: 7.5638
Larger area: Y
```

### Cálculos

**Triângulo X (3, 4, 5):**
```
p = (3 + 4 + 5) / 2 = 6
área = ?[6 × 3 × 2 × 1] = ?36 = 6.0000
```

**Triângulo Y (7.5, 4.5, 4.5):**
```
p = (7.5 + 4.5 + 4.5) / 2 = 8.25
área = ?[8.25 × 0.75 × 3.75 × 3.75]
área = ?[92.3438] ? 7.5638
```

---

## ?? Conceitos POO Aplicados

### 1. Encapsulamento

```java
private double a;  // Atributos privados
public double getA() { ... }  // Acesso controlado
```

Os atributos são privados, mas fornecemos getters para acesso controlado.

**Vantagem:** Protege os dados, evita modificações indevidas.

---

### 2. Classe e Objeto

**Classe:** É o **molde/planta**
```java
public class Triangle { ... }
```

**Objeto:** É uma **instância concreta**
```java
Triangle x = new Triangle(3, 4, 5);
Triangle y = new Triangle(7.5, 4.5, 4.5);
```

---

### 3. Construtor

Inicializa os dados quando o objeto é criado:
```java
public Triangle(double a, double b, double c) {
    this.a = a;
    this.b = b;
    this.c = c;
}
```

---

### 4. Métodos

Comportamento encapsulado:
```java
public double area() { ... }  // Calcula a área
public double getA() { ... }  // Acessa o atributo
```

---

## ?? Evolução do Código

### Versão 1 (Procedural - Sem POO)

```java
// ? Ruim: sem encapsulamento
double a1, b1, c1, a2, b2, c2;
// ... lê valores ...
double p1 = (a1 + b1 + c1) / 2;
double area1 = Math.sqrt(p1 * (p1 - a1) * (p1 - b1) * (p1 - c1));
double p2 = (a2 + b2 + c2) / 2;
double area2 = Math.sqrt(p2 * (p2 - a2) * (p2 - b2) * (p2 - c2));
```

**Problemas:** Código repetido, difícil manutenção, sem reutilização.

### Versão 2 (Com POO)

```java
// ? Bom: com encapsulamento
Triangle x = new Triangle(a1, b1, c1);
Triangle y = new Triangle(a2, b2, c2);
double areaX = x.area();
double areaY = y.area();
```

**Vantagens:** Código limpo, reutilizável, fácil manutenção.

---

## ?? Resumo dos Conceitos

| Conceito | Descrição |
|----------|-----------|
| **Classe** | Molde/tipo que define estrutura e comportamento |
| **Objeto** | Instância concreta de uma classe |
| **Atributo** | Variável que armazena dados do objeto |
| **Método** | Função que define o comportamento do objeto |
| **Construtor** | Método especial que inicializa o objeto |
| **Encapsulamento** | Proteção de dados com modificadores de acesso |
| **Private** | Acessível apenas dentro da classe |
| **Public** | Acessível de qualquer lugar |
| **This** | Referência ao próprio objeto |
| **Getter** | Método que fornece acesso a um atributo privado |

---

