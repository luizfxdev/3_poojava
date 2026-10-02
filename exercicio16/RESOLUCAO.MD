# Resolução: Programação Funcional e Lambdas em Java 17

## 1. Enunciado

1. Ler nome, email e salário de funcionários a partir de um arquivo `.csv`.
2. Mostrar, em ordem alfabética, o email dos funcionários cujo salário seja superior a um valor informado pelo usuário.
3. Mostrar a soma dos salários dos funcionários cujo nome começa com a letra `M`.

## 2. Estrutura do projeto

```
src/
├── entities/
│   └── Employee.java
└── application/
    └── Program.java
employees.csv
```

- `entities`: classes que representam o domínio (aqui, o funcionário).
- `application`: classe com o `main`, que orquestra a execução.
- Separar domínio de aplicação é uma decisão de organização (separação de responsabilidades): `Employee` não sabe nada sobre console ou arquivos.

## 3. Exemplo de entrada e saída

`employees.csv`:

```
Maria,maria@gmail.com,3200.00
Alex,alex@gmail.com,1900.00
Marco,marco@gmail.com,1700.00
Bob,bob@gmail.com,3500.00
Anna,anna@gmail.com,2800.00
```

Execução com salário `2000`:

```
Enter full file path: employees.csv
Enter salary: 2000
Email of people whose salary is more than 2000.00:
anna@gmail.com
bob@gmail.com
maria@gmail.com
Sum of salary of people whose name starts with 'M': 4900.00
```

A soma 4900.00 vem de Maria (3200) + Marco (1700).

---

## 4. `Employee.java` linha por linha

```java
package entities;
```
Declara o **pacote** da classe. Pacotes organizam classes e evitam conflito de nomes. O nome do pacote deve refletir a pasta onde o arquivo está (`src/entities`). Por convenção, pacotes são escritos em minúsculas.

```java
public class Employee {
```
- `public`: modificador de acesso. A classe pode ser usada por qualquer outra, inclusive de outro pacote (necessário, pois `Program` está em `application`).
- `class`: palavra reservada que declara uma classe.
- `Employee`: nome da classe, em **PascalCase** (convenção para classes). O nome do arquivo precisa ser idêntico ao da classe pública: `Employee.java`.
- Corresponde ao diagrama UML do enunciado.

```java
    private final String name;
    private final String email;
    private final Double salary;
```
Os **atributos** da classe, exatamente como no UML (`name: String`, `email: String`, `salary: Double`).

- `private`: encapsulamento. O estado interno só é acessível via métodos da própria classe. O sinal `-` do UML significa `private`.
- `final`: o valor só pode ser atribuído uma vez (no construtor). Isso torna o objeto **imutável**, o que combina muito bem com programação funcional: sem efeitos colaterais, um objeto não muda depois de criado, o que é seguro para usar em streams.
- `String`: classe da biblioteca padrão para texto.
- `Double` (e não `double`): usei o wrapper porque o diagrama UML especifica `Double`. `Double` é um objeto (pode ser `null`, funciona em coleções genéricas); `double` é tipo primitivo. Na prática, para cálculos o Java faz **unboxing** automático.
- Nomes em **camelCase** (convenção para atributos e variáveis).

```java
    public Employee(String name, String email, Double salary) {
        this.name = name;
        this.email = email;
        this.salary = salary;
    }
```
O **construtor**.
- Tem o mesmo nome da classe e não possui tipo de retorno.
- Recebe os três valores e inicializa os atributos. Como são `final`, a inicialização no construtor é obrigatória.
- `this.name = name;`: `this` referencia o objeto atual. Serve para diferenciar o atributo (`this.name`) do parâmetro (`name`), que têm o mesmo nome.

```java
    public static Employee fromCsvLine(String line) {
```
Um **método de fábrica estático** (*static factory method*).
- `static`: pertence à classe, não a uma instância. É chamado como `Employee.fromCsvLine(...)`, sem precisar de um objeto existente (afinal, ele é quem cria o objeto).
- Retorna `Employee`.
- Decisão de design: a responsabilidade de saber como transformar uma linha CSV em um funcionário fica na própria classe de domínio. Assim `Program` fica mais limpo e a conversão pode ser usada como **referência de método** (`Employee::fromCsvLine`) no `map`.

```java
        String[] fields = line.split(",");
```
- `split(",")` quebra a `String` em partes a cada vírgula e devolve um **array** de `String` (`String[]`).
- Para `"Maria,maria@gmail.com,3200.00"` obtemos `["Maria", "maria@gmail.com", "3200.00"]`.

```java
        String name = fields[0].trim();
        String email = fields[1].trim();
        Double salary = Double.parseDouble(fields[2].trim());
```
- Arrays em Java começam no índice `0`.
- `trim()` remove espaços nas pontas, tornando a leitura tolerante a `Maria, maria@gmail.com, 3200.00`.
- `Double.parseDouble(String)` converte texto em número decimal. Retorna `double` primitivo, que é convertido automaticamente (**autoboxing**) para `Double`. Se o texto não for numérico, lança `NumberFormatException`.
- Variáveis locais com nomes descritivos, uma por linha: é clean code (legibilidade acima de concisão).

```java
        return new Employee(name, email, salary);
    }
```
`new` instancia o objeto chamando o construtor; `return` devolve o resultado.

```java
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Double getSalary() { return salary; }
```
**Getters**: único meio de leitura dos atributos privados. Não criei *setters*, pois os atributos são `final` (imutabilidade). Seguem a convenção JavaBeans: `get` + nome do atributo com inicial maiúscula. Eles são importantes porque o código funcional usa `Employee::getEmail`, `Employee::getSalary` etc.

---

## 5. `Program.java` linha por linha

### Imports

```java
package application;

import entities.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
```
- `import` traz classes de outros pacotes para uso pelo nome simples.
- `entities.Employee`: nossa classe de domínio.
- `BufferedReader` e `FileReader`: leitura de arquivo. `FileReader` abre o arquivo; `BufferedReader` adiciona buffer (mais eficiente) e o método `lines()`, que devolve um `Stream<String>`.
- `IOException`: exceção checada lançada em falhas de entrada/saída.
- `List`, `Locale`, `Scanner`: lista, configuração regional e leitura do teclado.
- Não importei `Stream` nem `Collectors` porque usei `.toList()` (Java 16+), o que reduz imports e ruído.

### Classe e `main`

```java
public class Program {

    public static void main(String[] args) {
```
- `main` é o ponto de entrada da JVM: precisa ser `public static void main(String[] args)`.
- `static`: a JVM chama sem criar objeto. `void`: não retorna nada. `args`: argumentos de linha de comando (não usados).

```java
        Locale.setDefault(Locale.US);
```
Define o padrão regional como EUA. Isso faz o `Scanner` interpretar `2000.50` (ponto) como decimal e o `printf` imprimir com ponto. Sem isso, em um computador configurado para pt-BR, o `Scanner` esperaria vírgula e lançaria `InputMismatchException`.

```java
        try (Scanner scanner = new Scanner(System.in)) {
```
- **try-with-resources**: qualquer recurso que implemente `AutoCloseable` declarado nos parênteses é fechado automaticamente ao sair do bloco, mesmo com exceção. Evita esquecer o `close()`.
- `System.in` é a entrada padrão (teclado).

```java
            System.out.print("Enter full file path: ");
            String path = scanner.nextLine();

            System.out.print("Enter salary: ");
            double minimumSalary = scanner.nextDouble();
```
- `print` não quebra linha (o cursor fica ao lado da pergunta).
- `nextLine()` lê a linha inteira (necessário, pois caminhos podem ter espaços).
- `nextDouble()` lê um número decimal. Uso `double` primitivo aqui, pois é só um valor de comparação.
- Nome `minimumSalary` diz o papel da variável, ao invés de algo vago como `x` ou `value`.

```java
            List<Employee> employees = readEmployees(path);
```
`List<Employee>` usa **generics**: uma lista que só aceita `Employee`. Programar para a interface (`List`) e não para a implementação (`ArrayList`) é boa prática. A leitura foi extraída para um método próprio.

```java
            printEmailsAboveSalary(employees, minimumSalary);
            printSumOfSalariesByInitial(employees, 'M');
```
O `main` virou um roteiro legível: ler, mostrar emails, mostrar soma. Cada passo é um método com nome que explica a intenção (*clean code*: funções pequenas, com uma responsabilidade). `'M'` com aspas simples é um `char`; assim o método fica genérico e reutilizável para qualquer inicial.

```java
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
```
- `IOException` é **checada**: o compilador exige tratamento ou declaração com `throws`. Como `readEmployees` pode lançá-la, o `main` a trata aqui.
- `e.getMessage()` devolve a descrição do erro (ex.: arquivo não encontrado).

### Método `readEmployees`

```java
    private static List<Employee> readEmployees(String path) throws IOException {
```
- `private`: só usado dentro desta classe.
- `static`: não depende de estado de instância, então pode ser chamado direto do `main` (que também é estático).
- `throws IOException`: avisa que a exceção é repassada ao chamador.

```java
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
```
Outro try-with-resources, garantindo o fechamento do arquivo. O `new FileReader(path)` é "embrulhado" pelo `BufferedReader` (padrão *Decorator*).

```java
            return reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(Employee::fromCsvLine)
                    .toList();
```
Aqui está a **programação funcional**: um *pipeline* de stream.

- `reader.lines()`: cria um `Stream<String>` com cada linha do arquivo. Um **stream** é uma sequência de elementos processada de forma declarativa (você diz *o quê*, não *como* com `for`).
- `.filter(line -> !line.isBlank())`:
  - `filter` é uma **operação intermediária**: mantém só os elementos que satisfazem um `Predicate<T>` (função que retorna `boolean`).
  - `line -> !line.isBlank()` é uma **expressão lambda**: `parâmetro -> expressão`. O tipo de `line` é inferido (`String`).
  - `isBlank()` (Java 11+) é `true` para vazio ou só com espaços. Evita falha com linhas em branco no fim do arquivo.
- `.map(Employee::fromCsvLine)`:
  - `map` transforma cada elemento (`Function<T,R>`): de `String` para `Employee`.
  - `Employee::fromCsvLine` é uma **referência de método** (*method reference*), equivalente ao lambda `line -> Employee.fromCsvLine(line)`, só que mais enxuto.
- `.toList()`: **operação terminal**; dispara o processamento e coleta o resultado em uma `List` **imutável** (Java 16+). Antes era `.collect(Collectors.toList())`.
- Streams são **preguiçosos** (*lazy*): nada executa até a operação terminal.
- Como o `return` está dentro do `try`, o `reader` é fechado só depois de o stream ser consumido, que é o que queremos.

### Método `printEmailsAboveSalary`

```java
    private static void printEmailsAboveSalary(List<Employee> employees, double minimumSalary) {
        System.out.printf("Email of people whose salary is more than %.2f:%n", minimumSalary);
```
- `printf` usa formatação: `%.2f` = número decimal com 2 casas; `%n` = quebra de linha portável entre sistemas operacionais.

```java
        employees.stream()
                .filter(employee -> employee.getSalary() > minimumSalary)
                .map(Employee::getEmail)
                .sorted()
                .forEach(System.out::println);
```
- `employees.stream()`: cria um stream a partir da lista.
- `filter(employee -> employee.getSalary() > minimumSalary)`: lambda que usa a variável `minimumSalary` do método. Isso é uma **captura de variável**, permitida porque ela é *effectively final* (nunca é reatribuída). Lambdas só podem capturar variáveis finais ou efetivamente finais.
- `getSalary()` devolve `Double`; na comparação `>` ocorre o *unboxing* automático.
- `map(Employee::getEmail)`: transforma `Stream<Employee>` em `Stream<String>` (só os emails). Referência de método para um método de instância.
- `sorted()`: ordena. Para `String` usa a **ordem natural** (alfabética, via `Comparable`), sem precisar de comparador.
- `forEach(System.out::println)`: operação terminal que executa uma ação (`Consumer<T>`) para cada elemento. `System.out::println` é referência ao método `println` do objeto `System.out`.
- Ordem das operações importa: filtrar antes de mapear e ordenar reduz o volume processado.

### Método `printSumOfSalariesByInitial`

```java
    private static void printSumOfSalariesByInitial(List<Employee> employees, char initial) {
        double sum = employees.stream()
                .filter(employee -> employee.getName().charAt(0) == initial)
                .mapToDouble(Employee::getSalary)
                .sum();
```
- `charAt(0)`: pega o primeiro caractere do nome (índice 0). Comparação com `==` é correta aqui porque `char` é tipo primitivo (para `String` usaríamos `equals`).
- `mapToDouble(Employee::getSalary)`: variação de `map` que produz um `DoubleStream` (stream de primitivos `double`). Evita *boxing* e dá acesso a operações numéricas.
- `.sum()`: operação terminal de `DoubleStream`; soma tudo e retorna `double`. Para um stream vazio retorna `0.0`, sem exceção.
- A alternativa `reduce(0.0, Double::sum)` funciona, mas `mapToDouble().sum()` é mais clara e idiomática.

```java
        System.out.printf("Sum of salary of people whose name starts with '%c': %.2f%n", initial, sum);
    }
}
```
`%c` formata um `char`.

---

## 6. Decisões de design (resumo)

| Decisão | Motivo |
|---|---|
| Atributos `private final` + sem setters | Encapsulamento e imutabilidade, ideais com streams |
| `Employee.fromCsvLine` | Conversão fica no domínio e vira referência de método |
| Um método por responsabilidade | `main` legível, funções pequenas e reutilizáveis |
| try-with-resources | Fechamento seguro de `Scanner` e arquivo |
| `Locale.US` | Evita problema de vírgula/ponto decimal |
| `.toList()` | Java 16+, mais simples que `Collectors.toList()` |
| `mapToDouble().sum()` | Soma direta com stream primitivo |
| `initial` como parâmetro | Método reutilizável para qualquer letra |
| Sem comentários no código | Nomes expressivos dispensam comentários (clean code) |

## 7. Conceitos de programação funcional usados

- **Expressão lambda**: função anônima (`parâmetros -> corpo`).
- **Interface funcional**: interface com um único método abstrato (`Predicate`, `Function`, `Consumer`), alvo dos lambdas.
- **Referência de método**: atalho para lambdas que só chamam um método (`Classe::método`).
- **Stream**: pipeline declarativo com operações intermediárias (`filter`, `map`, `sorted`) e terminais (`forEach`, `toList`, `sum`).
- **Imutabilidade e ausência de efeitos colaterais**: a lista original nunca é alterada.

## 8. Como compilar e executar

```bash
mkdir out
javac -d out src/entities/Employee.java src/application/Program.java
java -cp out application.Program
```

Pontos de atenção: o CSV não tem cabeçalho; se tiver, pule a primeira linha com `.skip(1)` antes do `filter`. Nomes ou emails com vírgula dentro quebrariam o `split(",")`; para CSVs complexos use uma biblioteca (OpenCSV, Commons CSV).