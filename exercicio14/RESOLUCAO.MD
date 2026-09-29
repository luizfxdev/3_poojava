# Resolução: total de alunos do instrutor Alex com `Set`

## 1. Estrutura

```
exercicio-set/
└── Program.java
```

Uma única classe, sem pacote, para manter o exercício simples.

## 2. Ideia da solução

O enunciado diz que um aluno pode estar em vários cursos e que não se pode somar as quantidades. O que se quer é a **união** dos conjuntos de alunos dos cursos A, B e C.

Um `Set` não aceita elementos repetidos. Ao inserir o mesmo código de aluno duas vezes, a segunda inserção é ignorada. Logo, basta jogar os códigos de todos os cursos no mesmo `Set` e ler o tamanho no final.

Entrada esperada (formato clássico deste exercício):

```
How many students for course A? 3
21
17
19
How many students for course B? 2
17
22
How many students for course C? 3
19
22
30
Total students: 5
```

Os códigos 17, 19 e 22 aparecem em mais de um curso e são contados uma única vez: `{21, 17, 19, 22, 30}` = 5.

## 3. Explicação linha por linha

### Imports

```java
1  import java.util.HashSet;
2  import java.util.Scanner;
3  import java.util.Set;
```

- `import` permite usar classes de outros pacotes sem escrever o nome completo (`java.util.HashSet`).
- Os três tipos vêm de `java.util`, o pacote das coleções e utilitários.
- `Set` é a **interface**: define o contrato "coleção sem repetidos".
- `HashSet` é a **implementação** concreta de `Set`, baseada em tabela hash.
- `Scanner` lê dados da entrada (teclado).
- Importar apenas o que se usa, sem `java.util.*`, deixa claro quais dependências a classe tem.

### Declaração da classe

```java
5  public class Program {
```

- `class` declara uma classe. Em Java, todo código vive dentro de classes.
- `public` torna a classe visível para qualquer outra. Como é pública, o arquivo deve se chamar `Program.java` (nome da classe = nome do arquivo).
- `Program` segue a convenção PascalCase para classes.

### Método principal

```java
7      public static void main(String[] args) {
```

- Ponto de entrada: a JVM procura exatamente esta assinatura.
- `public`: a JVM precisa acessar o método de fora da classe.
- `static`: pertence à classe, não a um objeto. A JVM o chama sem criar `new Program()`.
- `void`: não retorna valor.
- `main`: nome fixo exigido pela JVM.
- `String[] args`: array com os argumentos de linha de comando. Não é usado aqui, mas a assinatura o exige.

### try-with-resources

```java
8          try (Scanner scanner = new Scanner(System.in)) {
```

- `Scanner` implementa `AutoCloseable`. Declarado entre parênteses do `try`, é fechado automaticamente ao sair do bloco, mesmo com exceção.
- Evita esquecer o `scanner.close()` e dispensa o `finally`.
- `new Scanner(System.in)` cria um leitor sobre a entrada padrão. `System.in` é o `InputStream` do teclado.
- `scanner` é uma variável local, em camelCase.

### Criação do conjunto final

```java
9              Set<Integer> alunosDoInstrutor = new HashSet<>();
```

- `Set<Integer>`: tipo da variável. `<Integer>` é um **generic**: o conjunto só aceita `Integer`, e o compilador rejeita qualquer outro tipo.
- Usa-se `Integer` e não `int` porque generics só trabalham com tipos de referência. O Java faz **autoboxing** (converte `int` em `Integer` automaticamente) na inserção.
- A variável é declarada com a **interface** (`Set`) e instanciada com a implementação (`HashSet`). Isso é programar para abstração: trocar por `LinkedHashSet` ou `TreeSet` mudaria só o lado direito.
- `new HashSet<>()`: o `<>` é o **operador diamante**, que deixa o compilador inferir `Integer` a partir do lado esquerdo.
- `HashSet` foi escolhido por ser o mais eficiente: `add` e `contains` custam O(1) em média. Não há necessidade de ordenação nem de manter ordem de inserção.
- O nome `alunosDoInstrutor` diz exatamente o que a variável guarda.

### Laço pelos cursos

```java
11             for (String curso : new String[]{"A", "B", "C"}) {
```

- É o **for-each** (laço aprimorado): percorre cada elemento de um array ou coleção, sem índice.
- `new String[]{"A", "B", "C"}` cria e inicializa um array de `String` inline.
- A cada volta, `curso` recebe "A", depois "B", depois "C".
- Evita repetir três vezes o mesmo bloco de leitura.

```java
12                 alunosDoInstrutor.addAll(lerAlunosDoCurso(scanner, curso));
```

- `lerAlunosDoCurso(scanner, curso)` retorna o `Set<Integer>` com os alunos daquele curso.
- `addAll(Collection)` insere todos os elementos do argumento no conjunto. Elementos já presentes são ignorados. É aqui que a **união** acontece.
- O `scanner` é passado por parâmetro para reaproveitar a mesma leitura de entrada e não criar outro `Scanner` sobre `System.in`.

```java
13             }
```

- Fecha o `for`.

### Saída

```java
15             System.out.println("Total students: " + alunosDoInstrutor.size());
```

- `size()` devolve a quantidade de elementos distintos do `Set`, que é a resposta do exercício.
- `+` concatena a `String` com o `int` retornado.
- `System.out.println` imprime e quebra a linha.

```java
16         }
17     }
```

- Linha 16 fecha o `try`: aqui o `Scanner` é fechado automaticamente.
- Linha 17 fecha o `main`.

### Método auxiliar

```java
19     private static Set<Integer> lerAlunosDoCurso(Scanner scanner, String curso) {
```

- `private`: só a própria classe usa. Detalhe de implementação não deve ser exposto.
- `static`: não depende de estado de objeto, e `main` (que é estático) só pode chamar métodos estáticos sem instância.
- `Set<Integer>`: tipo de retorno, a interface e não `HashSet`. Quem chama não precisa saber a implementação.
- Nome em camelCase, começando com verbo (`ler...`), descrevendo a ação.
- Parâmetros: `scanner` (fonte dos dados) e `curso` (usado só na mensagem).
- **Por que extrair o método:** o `main` fica com a visão geral (ler três cursos, unir, imprimir) e o detalhe da leitura fica isolado. Cada método faz uma coisa (princípio da responsabilidade única).

```java
20         System.out.print("How many students for course " + curso + "? ");
```

- `print` (sem `ln`) mantém o cursor na mesma linha, para o usuário digitar a quantidade logo após a pergunta.

```java
21         int quantidade = scanner.nextInt();
```

- `nextInt()` lê o próximo token da entrada e o converte em `int`. Ignora espaços e quebras de linha.
- `quantidade` é do tipo primitivo `int`, pois é só um contador.

```java
23         Set<Integer> alunos = new HashSet<>();
```

- Conjunto local, criado a cada chamada, que guarda só os alunos deste curso.
- Se fosse um `List`, o mesmo código poderia ser inserido duas vezes por erro de digitação. O `Set` já protege contra isso.

```java
24         for (int i = 0; i < quantidade; i++) {
25             alunos.add(scanner.nextInt());
26         }
```

- `for` tradicional, porque o número de repetições é conhecido (`quantidade`).
- `int i = 0`: contador inicial. `i < quantidade`: condição. `i++`: incremento.
- `scanner.nextInt()` lê um código de aluno; `add(...)` insere no conjunto com autoboxing de `int` para `Integer`.
- `add` retorna `boolean` (`true` se inseriu, `false` se já existia). Aqui o retorno é ignorado de propósito, pois repetido simplesmente não entra.
- A variável `i` existe só dentro do laço (escopo de bloco).

```java
27         return alunos;
28     }
29 }
```

- `return` devolve o conjunto ao chamador.
- Linha 28 fecha o método e a 29 fecha a classe.

## 4. Por que `Set` funciona para repetidos

`HashSet` usa `hashCode()` e `equals()` dos elementos. `Integer` já implementa os dois de forma correta (dois `Integer` com o mesmo valor são iguais e têm o mesmo hash). Por isso, inserir `17` duas vezes resulta em um único `17`.

Se o elemento fosse uma classe própria (por exemplo, `Aluno`), seria necessário sobrescrever `equals` e `hashCode`, senão dois objetos com o mesmo código seriam considerados diferentes.

## 5. Escolha da implementação de `Set`

| Implementação   | Ordem                     | Desempenho `add`/`contains` | Quando usar                          |
|-----------------|---------------------------|-----------------------------|--------------------------------------|
| `HashSet`       | Nenhuma garantida         | O(1) em média               | Só interessa unicidade (este caso)   |
| `LinkedHashSet` | Ordem de inserção         | O(1) em média               | Unicidade mantendo a ordem de entrada|
| `TreeSet`       | Ordenada (natural ou comparador) | O(log n)             | Unicidade com elementos ordenados    |

## 6. Como executar

```bash
javac Program.java
java Program
```