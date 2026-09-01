# Exercício: Sistema de Gestão de Funcionários ? Solução Completa

## Entendimento do Problema

Você precisa criar um sistema que:

1. **Leia** N funcionários (id, nome, salário)
2. **Armazene** em uma Lista
3. **Valide** se o ID existe (não repetir)
4. **Aumente** o salário de um funcionário por porcentagem
5. **Mostre** a listagem atualizada

**Restrição importante:** O salário é **private** ? só pode ser alterado via método `increaseSalary()`.

---

## Analogia LoL

Imagine um sistema de **ranks** de jogadores:

- Cada jogador tem `id` (código único), `name` (username), `elo` (rating)
- Você quer dar +10% de elo para um jogador específico
- Mas o elo **não pode ser mudado diretamente** ? só via método de ganho de LP

É exatamente isso!

---

## Planejamento da Solução

### Estrutura de Classes Necessárias

```
Employee (entidade)
??? id: int
??? name: String
??? salary: double (PRIVATE)
??? increaseSalary(percentage: double): void (MÉTODO)

Program (main)
??? Ler N
??? Ler N funcionários
??? Armazenar em List<Employee>
??? Ler id e percentual
??? Buscar funcionário na lista
??? Aumentar salário
??? Mostrar lista
```

### Por que List e não Array?

| Situação | Array | List |
|----------|-------|------|
| Sabe quantos funcionários? | Não | ? Sim |
| Vai buscar por ID? | Tedioso | ? Fácil com stream() |
| Vai adicionar/remover depois? | Impossível | ? Sim |

**Conclusão:** List é perfeito aqui.

---

## Código Completo

### Parte 1: Classe Employee (Entidade)

```java
public class Employee {
    
    // Atributos
    private int id;
    private String name;
    private double salary;
    
    // Construtor
    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
    
    // Getters (leitura)
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public double getSalary() {
        return salary;
    }
    
    // Método CRÍTICO: aumentar salário por porcentagem
    public void increaseSalary(double percentage) {
        this.salary += this.salary * percentage / 100.0;
    }
    
    // Método toString para exibição
    public String toString() {
        return id + ", " + name + ", " + String.format("%.2f", salary);
    }
}
```

---

### Parte 2: Programa Principal

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        
        // ============== FASE 1: ENTRADA DE DADOS ==============
        
        System.out.print("How many employees will be registered? ");
        int n = sc.nextInt();
        
        // Cria a lista vazia
        List<Employee> list = new ArrayList<>();
        
        // Loop para ler cada funcionário
        for (int i = 1; i <= n; i++) {
            System.out.println();
            System.out.println("Employee #" + i + ":");
            
            System.out.print("Id: ");
            int id = sc.nextInt();
            
            System.out.print("Name: ");
            sc.nextLine(); // Limpa o buffer do Enter anterior
            String name = sc.nextLine();
            
            System.out.print("Salary: ");
            double salary = sc.nextDouble();
            
            // Cria novo Employee e adiciona à lista
            list.add(new Employee(id, name, salary));
        }
        
        // ============== FASE 2: AUMENTO DE SALÁRIO ==============
        
        System.out.println();
        System.out.print("Enter the employee id that will have salary increase : ");
        int searchId = sc.nextInt();
        
        // Busca na lista o funcionário com esse id
        Employee emp = null;
        for (Employee e : list) {
            if (e.getId() == searchId) {
                emp = e;
                break;
            }
        }
        
        // Se encontrou
        if (emp != null) {
            System.out.print("Enter the percentage: ");
            double percentage = sc.nextDouble();
            emp.increaseSalary(percentage);
        } else {
            System.out.println("This id does not exist!");
        }
        
        // ============== FASE 3: SAÍDA ==============
        
        System.out.println();
        System.out.println("List of employees:");
        for (Employee e : list) {
            System.out.println(e);
        }
        
        sc.close();
    }
}
```

---

## Explicação Linha-por-Linha

### Setup Inicial

```java
Locale.setDefault(Locale.US);
```
**O quê:** Define o locale (região) como US para usar `.` como separador decimal
**Por quê:** Em português, `3.000,00` ? em US, `3000.00`. Scanner espera US.

```java
Scanner sc = new Scanner(System.in);
```
**O quê:** Cria leitor de entrada
**Por quê:** Para ler números e strings do teclado

---

### Criando a Lista

```java
List<Employee> list = new ArrayList<>();
```
**Quebra:**
- `List<Employee>`: Interface que define o tipo (lista de Employee)
- `ArrayList<>()`: Implementação (array dinâmico por trás)
- `<>`: Type inference (Java deduz que é Employee)

**Analogia:** Você criou uma "Bancada de Seleção" vazia. Agora vai adicionar campeões.

---

### Lendo os Funcionários

```java
for (int i = 1; i <= n; i++) {
    System.out.println("Employee #" + i + ":");
    
    int id = sc.nextInt();
    sc.nextLine(); // ?? CRÍTICO!
    String name = sc.nextLine();
    double salary = sc.nextDouble();
    
    list.add(new Employee(id, name, salary));
}
```

**Linha por linha:**

```java
for (int i = 1; i <= n; i++)
```
Loop de 1 até N. Manda ler N funcionários.

```java
int id = sc.nextInt();
```
Lê um inteiro (123)

```java
sc.nextLine(); // CRÍTICO!
```
**?? ARMADILHA COMUM:**

Quando você faz `sc.nextInt()`, ele lê o número MAS deixa o `\n` (Enter) na fila. Se você tentar `sc.nextLine()` logo depois, pega essa quebra de linha vazia.

```
Entrada do teclado: 333[Enter]

nextInt() lê:    333      (deixa [Enter] na fila)
nextLine() sem limpar: pega [Enter] vazio
                 ^
                 Problema!
```

**Solução:** Depois de `nextInt()`, sempre faça `sc.nextLine()` para limpar.

```java
String name = sc.nextLine();
```
Agora sim lê a linha inteira (ex: "Maria Brown")

```java
double salary = sc.nextDouble();
```
Lê o salário

```java
list.add(new Employee(id, name, salary));
```
**Cria novo Employee e adiciona à lista**

```
list = [Employee(333, "Maria", 4000), Employee(536, "Alex", 3000), ...]
```

---

### Procurando Funcionário

```java
Employee emp = null;
for (Employee e : list) {
    if (e.getId() == searchId) {
        emp = e;
        break;
    }
}
```

**O que faz:**
1. Começa com `emp = null` (ninguém encontrado)
2. Percorre cada Employee `e` da lista
3. Se o ID dele bate com `searchId`, salva em `emp` e **sai do loop**

**Por que `break`?** Se encontrou, não precisa continuar procurando!

---

### Aumentando o Salário

```java
if (emp != null) {
    double percentage = sc.nextDouble();
    emp.increaseSalary(percentage);
} else {
    System.out.println("This id does not exist!");
}
```

**Lógica:**
- Se achou (`emp != null`): lê percentual e chama o método
- Se não achou: mostra erro

**IMPORTANTE:** Note que `emp.increaseSalary(percentage)` é chamado. Isso significa que o objeto referenciado por `emp` é **modificado na lista**.

Você não precisa fazer `list.remove(emp)` e `list.add(emp)` novamente ? é a mesma referência!

---

### Exibindo o Resultado

```java
System.out.println("List of employees:");
for (Employee e : list) {
    System.out.println(e);
}
```

Usa `for-each` para percorrer e chama o método `toString()` de cada Employee.

O método `toString()` retorna:
```
333, Maria Brown, 4000.00
536, Alex Grey, 3300.00
772, Bob Green, 5000.00
```

---

## Fluxo Completo com Exemplo

### Entrada:
```
How many employees will be registered? 3

Employee #1:
Id: 333
Name: Maria Brown
Salary: 4000.00

Employee #2:
Id: 536
Name: Alex Grey
Salary: 3000.00

Employee #3:
Id: 772
Name: Bob Green
Salary: 5000.00

Enter the employee id that will have salary increase : 536
Enter the percentage: 10.0
```

### Execução Passo-a-Passo:

**Passo 1:** Cria lista vazia
```
list = []
```

**Passo 2:** Lê 3 funcionários
```
list = [
  Employee(333, "Maria Brown", 4000.00),
  Employee(536, "Alex Grey", 3000.00),
  Employee(772, "Bob Green", 5000.00)
]
```

**Passo 3:** Procura por ID 536
```
Loop:
  e.getId() == 333? Não
  e.getId() == 536? SIM! ? emp = Employee(536, "Alex Grey", 3000.00)
  break
```

**Passo 4:** Aumenta salário de Alex em 10%
```
emp.increaseSalary(10.0)
  this.salary = 3000.00 + (3000.00 * 10.0 / 100.0)
  this.salary = 3000.00 + 300.00
  this.salary = 3300.00
```

**Passo 5:** Exibe
```
List of employees:
333, Maria Brown, 4000.00
536, Alex Grey, 3300.00
772, Bob Green, 5000.00
```

---

## Conceitos-Chave Aplicados

### 1. Encapsulamento (private/public)

```java
private double salary;  // Não pode acessar direto

public double getSalary() { return salary; }  // Ler ok
public void increaseSalary(double percentage) { ... }  // Aumentar via método
```

**Por quê?** Protege a lógica. Se alguém faz `emp.salary = -1000`, quebra tudo. Com método, você valida.

---

### 2. List vs Array

| Operação | Array | List |
|----------|-------|------|
| Declarar | `new Employee[3]` | `new ArrayList<>()` |
| Adicionar | `array[0] = emp` | `list.add(emp)` |
| Tamanho | `.length` | `.size()` |
| Percorrer | `for (int i...)` | `for (Employee e...)` |
| Buscar | Loop manual | `stream().filter()` |

**Aqui:** List é melhor porque não sabe quantos funcionários serão registrados.

---

### 3. For-Each

```java
for (Employee e : list) {
    System.out.println(e);
}
```

**Lê-se:** "Para cada Employee `e` contido em `list`, faça..."

**Equivalente:**
```java
for (int i = 0; i < list.size(); i++) {
    Employee e = list.get(i);
    System.out.println(e);
}
```

For-each é mais limpo quando não precisa do índice.

---

### 4. Referência vs Cópia

```java
Employee emp = null;

// Dentro do loop:
if (e.getId() == searchId) {
    emp = e;  // Não copia, aponta para o mesmo objeto
}

// Depois:
emp.increaseSalary(10);  // Modifica o objeto original na lista
```

**Importante:** `emp` e o objeto dentro de `list` apontam para o **mesmo** lugar na memória. Modificar um modifica o outro!

```
list[1] ???
          ???? [Employee: Alex, 3300.00]
emp ???????
```

---

## Tratamento de Erros: Caso ID Não Exista

```java
Employee emp = null;
for (Employee e : list) {
    if (e.getId() == searchId) {
        emp = e;
        break;
    }
}

if (emp != null) {
    // Faz o aumento
} else {
    System.out.println("This id does not exist!");
}
```

**Fluxo:**
1. Se loop encontra, `emp` recebe referência
2. Se loop não encontra, `emp` fica null
3. Verifica: `emp != null`?
   - Sim ? aumenta
   - Não ? mostra erro

---

## Versão Alternativa com Stream (Mais Moderno)

```java
System.out.print("Enter the employee id that will have salary increase : ");
int searchId = sc.nextInt();

Employee emp = list.stream()
    .filter(e -> e.getId() == searchId)
    .findFirst()
    .orElse(null);

if (emp != null) {
    System.out.print("Enter the percentage: ");
    double percentage = sc.nextDouble();
    emp.increaseSalary(percentage);
} else {
    System.out.println("This id does not exist!");
}
```

**O que mudou:**
- `.stream()`: transforma lista em stream (pipeline)
- `.filter(e -> e.getId() == searchId)`: filtra quem tem esse ID
- `.findFirst()`: pega o primeiro (ou vazio)
- `.orElse(null)`: se vazio, retorna null

**Resultado:** Mesma coisa, mas em uma linha. Mais elegante? Sim. Necessário? Não.

---

## Checklist de Aprendizado

- [ ] Entendo por que usar List em vez de Array
- [ ] Sei que `salary` é private ? não pode acessar direto
- [ ] Entendo o método `increaseSalary()` que aumenta por porcentagem
- [ ] Sei a diferença entre `nextInt()` e `nextLine()` e quando limpar o buffer
- [ ] Entendo for-each (`for (Employee e : list)`)
- [ ] Sei buscar um elemento na lista via loop
- [ ] Entendo que modificar `emp` modifica o objeto na lista
- [ ] Consigo validar se um ID existe
- [ ] Sei usar `toString()` para exibir dados formatados
- [ ] Consigo combinar tudo em um programa funcional

---

## Analogia Final: LoL (Bancada + Draft)

```
List<Employee> ~ Bancada de Seleção de Campeões

1. Cria bancada vazia
   List<Employee> bancada = new ArrayList<>();

2. Adiciona 3 campeões
   bancada.add(new Employee(1, "Garen", 5000));
   bancada.add(new Employee(2, "Ahri", 4500));
   bancada.add(new Employee(3, "Ashe", 4800));

3. Busca um campeão pelo ID
   for (Employee champ : bancada) {
       if (champ.getId() == 2) { // Ahri
           champ.increaseSalary(10); // Dá bônus de 10%
       }
   }

4. Mostra bancada atualizada
   for (Employee champ : bancada) {
       System.out.println(champ);
   }
   // 1, Garen, 5000.00
   // 2, Ahri, 4950.00  ? aumentou 10%
   // 3, Ashe, 4800.00
```

---

## Resumo Final

| Conceito | Aplicação Aqui |
|----------|-----------------|
| **List** | Armazenar N funcionários dinamicamente |
| **Encapsulamento** | Salary é private, altera via método |
| **For-each** | Percorrer lista de forma limpa |
| **Referência** | Modificar emp modifica objeto na lista |
| **Validação** | Verificar se ID existe antes de aumentar |
| **toString()** | Exibir dados formatados |

Pronto! Você está pronto para resolver qualquer exercício com listas e objetos.