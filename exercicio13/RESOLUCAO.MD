# Resolução — Generics em Java (PrintService)

## Enunciado

Ler `N` e depois `N` inteiros, imprimir a lista organizada (`[10, 8, 23]`) e informar o primeiro valor lido, usando um serviço genérico `PrintService` com `addValue`, `first` e `print`.

## Por que Generics

O diagrama pede um `PrintService` reutilizável. Sem generics, ele teria que ser escrito para `int` especificamente (ou usar `Object` e forçar casts inseguros). Com `class PrintService<T>`, a mesma classe serve para `Integer`, `String`, `Cliente`, etc. `T` é um **parâmetro de tipo**: um espaço reservado que só é preenchido quando a classe é instanciada (`new PrintService<Integer>()`).

---

## `PrintService.java`

```java
import java.util.ArrayList;
import java.util.List;

public class PrintService<T> {

    private final List<T> values = new ArrayList<>();

    public void addValue(T value) {
        values.add(value);
    }

    public T first() {
        return values.get(0);
    }

    public void print() {
        System.out.println(values);
    }
}
```

### Linha a linha

- **`public class PrintService<T>`** — declara a classe genérica. `<T>` é a convenção para "Type" (poderia ser qualquer letra, mas `T` é o padrão da comunidade Java). A partir daqui, `T` pode ser usado no corpo da classe como se fosse um tipo real.

- **`private final List<T> values = new ArrayList<>();`**
  - `List<T>` — a lista guarda elementos do tipo que for definido na instanciação. Isso é generics compondo com generics: `List` já é genérica na própria JDK, e aqui ela é parametrizada com o `T` da classe.
  - `private` — encapsulamento: ninguém fora da classe acessa `values` diretamente, só através dos métodos públicos (`addValue`, `first`, `print`).
  - `final` — a referência da lista nunca é reatribuída depois de criada; só seu conteúdo muda (via `add`). Isso comunica intenção e evita bugs de reatribuição acidental.
  - `new ArrayList<>()` — **diamond operator** (`<>`). O compilador infere que é `ArrayList<T>` a partir do tipo declarado à esquerda, então não é preciso repetir o tipo.

- **`public void addValue(T value)`** — recebe um parâmetro do tipo `T` e adiciona à lista. Como `T` é resolvido em tempo de compilação, `addValue("texto")` seria erro de compilação se `T` for `Integer` — isso é a principal vantagem sobre usar `Object`: segurança de tipos sem casts manuais.

- **`public T first()`** — retorna o tipo `T`, não `Object`. Quem chama `printService.first()` já recebe um `Integer` pronto, sem precisar fazer `(Integer) result`. Não há verificação de lista vazia porque o enunciado garante que `N` valores são inseridos antes da chamada — manter o método simples, sem tratamento defensivo que o exercício não pede.

- **`public void print()`** — imprime `values` diretamente. `List.toString()` já formata como `[10, 8, 23]`, que é exatamente o formato pedido no exemplo — por isso não há necessidade de montar a string manualmente.

---

## `Main.java`

```java
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many values? ");
        int amount = Integer.parseInt(scanner.nextLine());

        PrintService<Integer> printService = new PrintService<>();

        for (int i = 0; i < amount; i++) {
            int value = Integer.parseInt(scanner.nextLine());
            printService.addValue(value);
        }

        printService.print();
        System.out.println("First: " + printService.first());

        scanner.close();
    }
}
```

### Linha a linha

- **`Scanner scanner = new Scanner(System.in);`** — lê a entrada padrão (teclado). É criado uma única vez e reutilizado em todo o método, evitando abrir múltiplos streams sobre `System.in`.

- **`System.out.print("How many values? ");`** — `print` (não `println`) para o cursor ficar na mesma linha do prompt, replicando o layout do exemplo (`How many values? 3`).

- **`int amount = Integer.parseInt(scanner.nextLine());`** — `nextLine()` lê a linha inteira digitada e `Integer.parseInt` converte de `String` para `int`. Prefere-se `nextLine()` a `nextInt()` aqui para manter um único padrão de leitura em todo o programa (evita o problema clássico de misturar `nextInt()` com `nextLine()`, que deixa `\n` pendente no buffer).

- **`PrintService<Integer> printService = new PrintService<>();`** — aqui o generics é **instanciado**: `T` vira `Integer`. A partir deste ponto, o compilador trata `printService.addValue(...)` como se o método fosse `addValue(Integer value)`, e `printService.first()` como se retornasse `Integer`. `Integer` (wrapper) é usado, e não `int` (primitivo), porque tipos genéricos em Java não aceitam primitivos — só tipos de referência. A conversão `int → Integer` é automática (autoboxing) na chamada `addValue(value)`.

- **`for (int i = 0; i < amount; i++)`** — laço simples e direto para ler exatamente `N` valores, condizente com "leia uma quantidade N, e depois N números".

- **`int value = Integer.parseInt(scanner.nextLine());`** — mesma lógica de conversão do `amount`, agora para cada número da lista.

- **`printService.addValue(value);`** — delega o armazenamento ao serviço, mantendo `Main` responsável só pela leitura/orquestração e `PrintService` responsável pelo armazenamento/exibição (separação de responsabilidades, coerente com o diagrama de classes do exercício).

- **`printService.print();`** — imprime `[10, 8, 23]`.

- **`System.out.println("First: " + printService.first());`** — concatena `String` com `Integer`; o `+` aciona `String.valueOf` implicitamente sobre o retorno de `first()`. Resultado: `First: 10`.

- **`scanner.close();`** — fecha o recurso ao final, boa prática para liberar o stream de entrada.

---

## Conceitos de Generics aplicados

| Conceito | Onde aparece | Papel |
|---|---|---|
| Parâmetro de tipo (`<T>`) | `class PrintService<T>` | Torna a classe reutilizável para qualquer tipo de referência |
| Tipo parametrizado | `List<T>`, `PrintService<Integer>` | Composição/instanciação de tipos genéricos |
| Diamond operator (`<>`) | `new ArrayList<>()`, `new PrintService<>()` | Evita repetição do tipo, inferido pelo contexto |
| Autoboxing | `addValue(value)` com `value` do tipo `int` | Converte `int` → `Integer` automaticamente para caber em `T` |
| Type erasure (implícito) | Comportamento em tempo de execução | Em bytecode, `T` vira `Object` com casts inseridos pelo compilador — é por isso que `T` não pode ser primitivo nem instanciado diretamente (`new T()`) |

## Por que essa estrutura e não outra

- Poderia existir uma verificação de lista vazia em `first()`, mas isso agregaria tratamento de erro que o exercício não exige — o enunciado garante `N` valores antes da leitura de `first`.
- Poderia usar `var` em vez dos tipos explícitos, mas manter os tipos explícitos (`int`, `PrintService<Integer>`) deixa mais didático para quem está aprendendo generics, já que o tipo parametrizado fica visível.
- `PrintService<Integer>` (e não `PrintService<int>`) porque Java generics não suportam tipos primitivos — só classes wrapper.