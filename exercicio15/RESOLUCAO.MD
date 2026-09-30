# Consolidação de votos com `Map` em Java 17

## 1. Enunciado

Em uma eleição, cada urna gera vários registros no formato `.csv` com o **nome do candidato** e a **quantidade de votos** obtida naquela urna. O programa deve ler os registros de um arquivo e gerar um **relatório consolidado** com o total de cada candidato.

O mesmo candidato aparece várias vezes (uma linha por urna). O problema central é: **somar valores agrupados por uma chave**. Isso é o caso de uso clássico de `Map`.

---

## 2. Estrutura do projeto

```
votacao/
├── dados/
│   └── votos.csv
└── src/main/java/votacao/
    ├── RegistroVotacao.java
    ├── LeitorRegistros.java
    ├── ConsolidadorVotos.java
    ├── RelatorioVotacao.java
    └── Main.java
```

### Por que 5 classes para um exercício pequeno?

Cada classe tem **uma única responsabilidade** (princípio SRP do Clean Code):

| Classe | Responsabilidade |
|---|---|
| `RegistroVotacao` | Representar uma linha do CSV (dado) |
| `LeitorRegistros` | Ler o arquivo e convertê-lo em objetos |
| `ConsolidadorVotos` | Somar votos por candidato (regra de negócio, usa `Map`) |
| `RelatorioVotacao` | Exibir o resultado |
| `Main` | Orquestrar o fluxo |

Se amanhã o CSV virar um banco de dados, só `LeitorRegistros` muda. Se o relatório virar um PDF, só `RelatorioVotacao` muda.

### Formato do CSV (`dados/votos.csv`)

```
Ana Souza,120
Bruno Lima,95
Carla Mendes,143
Ana Souza,80
...
```

Sem cabeçalho, separador vírgula, duas colunas: `candidato,votos`.

---

## 3. `RegistroVotacao.java`

```java
package votacao;

public record RegistroVotacao(String candidato, int votos) {

    public RegistroVotacao {
        if (candidato == null || candidato.isBlank()) {
            throw new IllegalArgumentException("Nome do candidato é obrigatório");
        }
        if (votos < 0) {
            throw new IllegalArgumentException("Quantidade de votos não pode ser negativa");
        }
    }
}
```

### Linha 1: `package votacao;`
Declara o **pacote** da classe. Pacote é um "namespace": agrupa classes relacionadas e evita conflito de nomes. Por convenção, é escrito em minúsculas e deve refletir a estrutura de pastas (`src/main/java/votacao/`).

### Linha 3: `public record RegistroVotacao(String candidato, int votos) {`
- `public`: modificador de acesso. A classe é visível para qualquer outra classe.
- `record`: recurso do Java 16+ (estável no 17) para criar **classes imutáveis de dados**. O compilador gera automaticamente:
  - campos `private final` (`candidato` e `votos`);
  - construtor canônico com todos os campos;
  - métodos de acesso `candidato()` e `votos()` (**sem** o prefixo `get`);
  - `equals()`, `hashCode()` e `toString()`.
- `RegistroVotacao`: nome da classe em **PascalCase** (convenção Java para tipos).
- `(String candidato, int votos)`: **componentes** do record. São os atributos do objeto.
  - `String candidato`: texto, nome do candidato.
  - `int votos`: tipo primitivo inteiro. Usei `int` (e não `Integer`) porque o voto nunca é nulo, e primitivos evitam *autoboxing* desnecessário.

**Decisão:** usar `record` em vez de uma classe comum elimina ~25 linhas de código repetitivo (construtor, getters, equals, hashCode, toString) e garante **imutabilidade**: depois de criado, o registro não muda. Isso é uma boa prática para objetos que só transportam dados.

### Linha 5: `public RegistroVotacao {`
É o **construtor compacto** do record. Repare que:
- não há parênteses nem lista de parâmetros (eles vêm do cabeçalho do record);
- o corpo roda **antes** da atribuição automática dos campos, sendo o lugar ideal para **validação**.

### Linhas 6 a 8: validação do candidato
```java
if (candidato == null || candidato.isBlank()) {
    throw new IllegalArgumentException("Nome do candidato é obrigatório");
}
```
- `candidato == null`: verifica se a referência é nula. Comparamos objetos com `==` apenas para checar `null`; para comparar conteúdo de `String` usa-se `.equals()`.
- `||`: OU lógico com **curto-circuito**. Se a primeira condição for verdadeira, a segunda nem é avaliada. Isso é essencial: se `candidato` for `null`, chamar `candidato.isBlank()` lançaria `NullPointerException`. A ordem importa.
- `isBlank()`: método de `String` (Java 11+). Retorna `true` se a string for vazia **ou** só tiver espaços. Diferente de `isEmpty()`, que só detecta string com tamanho 0.
- `throw new IllegalArgumentException(...)`: lança uma exceção **não verificada** (unchecked, filha de `RuntimeException`) indicando que o argumento recebido é inválido. Não precisa de `throws` na assinatura.

**Decisão:** falhar **cedo** (*fail fast*). É melhor rejeitar um dado inválido na criação do objeto do que descobrir o problema no meio do relatório.

### Linhas 9 a 11: validação dos votos
```java
if (votos < 0) {
    throw new IllegalArgumentException("Quantidade de votos não pode ser negativa");
}
```
Voto negativo não existe. Sem essa validação, um dado corrompido reduziria silenciosamente o total de um candidato.

---

## 4. `LeitorRegistros.java`

```java
package votacao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LeitorRegistros {

    private static final String SEPARADOR = ",";

    public List<RegistroVotacao> ler(Path arquivo) throws IOException {
        return Files.readAllLines(arquivo).stream()
                .filter(linha -> !linha.isBlank())
                .map(this::converter)
                .toList();
    }

    private RegistroVotacao converter(String linha) {
        String[] campos = linha.split(SEPARADOR);
        if (campos.length != 2) {
            throw new IllegalArgumentException("Linha inválida: " + linha);
        }
        String candidato = campos[0].trim();
        int votos = Integer.parseInt(campos[1].trim());
        return new RegistroVotacao(candidato, votos);
    }
}
```

### Linhas 3 a 6: `import`
`import` traz classes de outros pacotes para uso pelo nome simples.
- `java.io.IOException`: exceção **verificada** (checked) lançada em falhas de leitura/escrita.
- `java.nio.file.Files`: classe utilitária (só métodos estáticos) para operações com arquivos.
- `java.nio.file.Path`: representa um caminho de arquivo. Substitui o antigo `java.io.File` na API moderna (NIO.2).
- `java.util.List`: interface de lista ordenada.

**Decisão:** imports explícitos, sem `import java.util.*;`. Deixa claro o que a classe realmente usa.

### Linha 8: `public class LeitorRegistros {`
Classe comum. O nome é um **substantivo** que indica o papel (leitor de registros). Classes devem ter nomes de substantivos; métodos, de verbos.

### Linha 10: `private static final String SEPARADOR = ",";`
Cada palavra tem um significado:
- `private`: só esta classe enxerga a constante (**encapsulamento**).
- `static`: pertence à **classe**, não a cada objeto. Existe uma única cópia na memória.
- `final`: o valor não pode ser reatribuído. Junto com `static`, forma uma **constante**.
- `String`: tipo texto.
- `SEPARADOR`: nome em `UPPER_SNAKE_CASE`, convenção para constantes.

**Decisão:** evitar *magic strings*. Escrever `","` solto no código não diz o que ele significa. Com a constante, `linha.split(SEPARADOR)` se explica sozinho, e a troca para `;` seria feita em um único lugar.

### Linha 12: `public List<RegistroVotacao> ler(Path arquivo) throws IOException {`
- `public`: será chamado por outras classes.
- `List<RegistroVotacao>`: tipo de retorno. `List` é uma **interface**; `<RegistroVotacao>` é um **generic** que diz que a lista só contém esse tipo. O compilador impede inserir outro tipo e dispensa *casts*.
- `ler`: nome do método, um verbo no infinitivo, em `camelCase`.
- `Path arquivo`: parâmetro que recebe o caminho do arquivo.
- `throws IOException`: declara que o método **pode lançar** essa exceção verificada. Quem chamar precisa tratá-la ou repassá-la.

**Decisão:** declarar `throws` em vez de capturar com `try/catch` aqui. O leitor não sabe o que fazer com um erro de arquivo (mostrar mensagem? tentar outro arquivo?). Quem decide é quem orquestra, no caso o `Main`. Capturar e ignorar seria pior.

### Linhas 13 a 16: o *pipeline* com Stream
```java
return Files.readAllLines(arquivo).stream()
        .filter(linha -> !linha.isBlank())
        .map(this::converter)
        .toList();
```
- `Files.readAllLines(arquivo)`: lê o arquivo inteiro e devolve `List<String>`, uma string por linha. Usa UTF-8 por padrão. Adequado a arquivos de tamanho moderado; para arquivos gigantes, usar `Files.lines()` (leitura sob demanda).
- `.stream()`: transforma a lista em um `Stream<String>`, uma sequência de elementos sobre a qual encadeamos operações.
- `.filter(linha -> !linha.isBlank())`: mantém só as linhas que satisfazem a condição.
  - `linha -> ...` é uma **expressão lambda**: função anônima onde `linha` é o parâmetro.
  - `!` nega. Ignoramos linhas em branco (por exemplo, a linha vazia no fim do arquivo), que quebrariam o `split`.
- `.map(this::converter)`: transforma cada `String` em um `RegistroVotacao`.
  - `this::converter` é uma **referência a método**, equivalente a `linha -> this.converter(linha)`, porém mais limpa.
- `.toList()`: operação terminal (Java 16+) que coleta o resultado em uma lista **imutável**. Substitui o antigo `.collect(Collectors.toList())`.

**Decisão:** stream em vez de `for` com `add()`. O código descreve **o quê** (filtrar, converter, coletar) e não **como** (índices, lista auxiliar, `add`).

### Linha 19: `private RegistroVotacao converter(String linha) {`
Método auxiliar `private`: detalhe interno de implementação, não faz parte da API pública da classe.

### Linha 20: `String[] campos = linha.split(SEPARADOR);`
- `String[]`: **array** de strings.
- `split(",")`: quebra a linha a cada vírgula. `"Ana Souza,120"` vira `["Ana Souza", "120"]`.

### Linhas 21 a 23: validação estrutural
```java
if (campos.length != 2) {
    throw new IllegalArgumentException("Linha inválida: " + linha);
}
```
- `campos.length`: atributo (sem parênteses) que informa o tamanho do array. Em `String`, `length()` é método (com parênteses); em array, é atributo.
- A mensagem inclui a linha problemática, o que acelera o diagnóstico.

### Linha 24: `String candidato = campos[0].trim();`
- `campos[0]`: índice zero é a primeira posição.
- `trim()`: remove espaços do início e do fim. Torna `"Ana Souza "` igual a `"Ana Souza"`, evitando que o `Map` trate o mesmo candidato como duas chaves diferentes.

### Linha 25: `int votos = Integer.parseInt(campos[1].trim());`
- `Integer.parseInt(String)`: converte texto em `int`. Lança `NumberFormatException` se o texto não for um número.
- O `trim()` também é necessário: `" 120"` falharia sem ele.

### Linha 26: `return new RegistroVotacao(candidato, votos);`
`new` cria o objeto. É neste momento que o construtor compacto do record executa a validação.

---

## 5. `ConsolidadorVotos.java` (o coração do exercício)

```java
package votacao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsolidadorVotos {

    public Map<String, Integer> consolidar(List<RegistroVotacao> registros) {
        Map<String, Integer> totais = new HashMap<>();
        for (RegistroVotacao registro : registros) {
            totais.merge(registro.candidato(), registro.votos(), Integer::sum);
        }
        return totais;
    }
}
```

### O que é um `Map`?
Estrutura que associa **chave → valor**. Cada chave é **única**. Aqui:

| Chave (`String`) | Valor (`Integer`) |
|---|---|
| `"Ana Souza"` | `245` |
| `"Bruno Lima"` | `205` |

Buscar por chave é praticamente instantâneo (O(1) em média no `HashMap`). É a estrutura natural quando precisamos de "total **por** candidato".

### Linha 3 a 5: imports
- `Map`: a **interface** (o contrato).
- `HashMap`: a **implementação** concreta.

### Linha 9: `public Map<String, Integer> consolidar(List<RegistroVotacao> registros) {`
- Retorna `Map<String, Integer>`: chave `String` (candidato), valor `Integer` (total de votos).
- **Por que `Integer` e não `int`?** Generics só aceitam **tipos de referência**, não primitivos. `Map<String, int>` não compila. `Integer` é a classe *wrapper* de `int`; a conversão entre os dois é o **autoboxing/unboxing**, feita automaticamente.
- O parâmetro é `List<RegistroVotacao>`: **programar para a interface**, não para `ArrayList`. Qualquer lista serve.

### Linha 10: `Map<String, Integer> totais = new HashMap<>();`
- **Tipo da variável é a interface** (`Map`), **o objeto é a implementação** (`HashMap`). Isso permite trocar `HashMap` por `TreeMap` sem alterar mais nada.
- `<>` é o **operador diamante**: o compilador infere `<String, Integer>` do lado esquerdo.
- `totais` é o nome da variável: plural, pois é um conjunto de totais.

**Por que `HashMap` e não `TreeMap` ou `LinkedHashMap`?**

| Implementação | Ordem das chaves | Custo de busca | Quando usar |
|---|---|---|---|
| `HashMap` | Nenhuma garantida | O(1) | Padrão, quando a ordem não importa |
| `LinkedHashMap` | Ordem de inserção | O(1) | Quando a ordem de chegada importa |
| `TreeMap` | Ordem natural das chaves (alfabética) | O(log n) | Quando precisa das chaves ordenadas |

Ordenamos apenas na hora de exibir (por votos, não por nome), então `HashMap` é a escolha mais simples e rápida.

### Linhas 11 a 13: laço `for-each`
```java
for (RegistroVotacao registro : registros) {
    totais.merge(registro.candidato(), registro.votos(), Integer::sum);
}
```
- `for (Tipo item : colecao)`: lê-se "para cada `registro` em `registros`". Sem índice e sem risco de estourar limites.
- `registro.candidato()`: chamada ao método de acesso gerado pelo record (sem `get`).

### O método `merge` (a decisão mais importante)
```java
totais.merge(chave, valor, funcaoDeCombinacao);
```
Comportamento:
1. Se a **chave não existe**, insere `chave → valor`.
2. Se a **chave já existe**, calcula `funcaoDeCombinacao(valorAntigo, valorNovo)` e grava o resultado.

`Integer::sum` é uma referência a método equivalente a `(a, b) -> a + b`.

Exemplo com "Ana Souza":
- 1ª vez (120): não existe, guarda `120`.
- 2ª vez (80): existe, `sum(120, 80)` = `200`.
- 3ª vez (45): existe, `sum(200, 45)` = `245`.

**Alternativa que evitamos** (a forma "manual"):
```java
if (totais.containsKey(nome)) {
    totais.put(nome, totais.get(nome) + votos);
} else {
    totais.put(nome, votos);
}
```
Faz duas buscas no mapa, tem cinco linhas e mais chance de erro. Existe também `getOrDefault(nome, 0) + votos`, que é boa, mas `merge` expressa a intenção ("combine com o que já existe") de forma mais direta.

### Linha 14: `return totais;`
Devolve o mapa preenchido.

---

## 6. `RelatorioVotacao.java`

```java
package votacao;

import java.util.Comparator;
import java.util.Map;

public class RelatorioVotacao {

    public void imprimir(Map<String, Integer> totais) {
        System.out.println("RELATÓRIO CONSOLIDADO DE VOTAÇÃO");
        totais.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entrada -> System.out.printf("%-20s %d%n", entrada.getKey(), entrada.getValue()));
        System.out.printf("%-20s %d%n", "TOTAL", somar(totais));
    }

    private int somar(Map<String, Integer> totais) {
        return totais.values().stream().mapToInt(Integer::intValue).sum();
    }
}
```

### Linha 8: `public void imprimir(Map<String, Integer> totais) {`
- `void`: o método não devolve valor, apenas executa uma ação (efeito colateral: escrever no console).
- O parâmetro é `Map` (interface), então funciona com qualquer implementação.

### Linha 9: `System.out.println(...)`
`System.out` é o fluxo de saída padrão (console). `println` imprime e quebra a linha.

### Linha 10: `totais.entrySet().stream()`
- `entrySet()`: devolve um `Set<Map.Entry<String, Integer>>`. Cada **`Entry`** é um par chave/valor, acessado por `getKey()` e `getValue()`.
- Um `Map` não é uma `Collection`, então **não tem `.stream()` direto**. Precisamos escolher a "visão" do mapa: `keySet()` (só chaves), `values()` (só valores) ou `entrySet()` (pares).
- Usamos `entrySet()` porque precisamos de **chave e valor juntos**.

### Linhas 11 e 12: ordenação
```java
.sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
        .thenComparing(Map.Entry.comparingByKey()))
```
- `sorted(comparator)`: ordena o stream usando um `Comparator`.
- `Map.Entry.comparingByValue(...)`: cria um comparador que compara as entradas pelo **valor** (votos).
- `Comparator.reverseOrder()`: inverte para ordem **decrescente** (quem tem mais votos primeiro).
- `Map.Entry.<String, Integer>`: o trecho `<String, Integer>` antes do nome do método é uma **type witness** (indicação explícita de tipos genéricos). Sem ela, o compilador não consegue inferir os tipos por causa do encadeamento.
- `.thenComparing(Map.Entry.comparingByKey())`: **critério de desempate**. Se dois candidatos empatam em votos, ordena por nome (alfabético). Garante saída **determinística**: mesmo entrada, mesma saída.

**Decisão:** o `HashMap` não garante ordem. Por isso a ordenação acontece na apresentação, o que separa a **regra de negócio** (somar) da **regra de exibição** (ordenar).

### Linha 13: `.forEach(...)`
Operação terminal: executa a ação para cada elemento, já ordenado.
```java
entrada -> System.out.printf("%-20s %d%n", entrada.getKey(), entrada.getValue())
```
- `printf` imprime com **formatação**:
  - `%-20s`: texto (`s`) com largura mínima de 20 caracteres, **alinhado à esquerda** (`-`). Cria colunas alinhadas.
  - `%d`: número inteiro decimal.
  - `%n`: quebra de linha **independente de sistema operacional** (melhor que `\n`).
- `entrada.getKey()` é o nome; `entrada.getValue()` é o total (o `Integer` é convertido para o `%d` sem esforço).

### Linha 14: linha do total geral
Reaproveita `printf` e chama o método privado `somar`.

### Linhas 17 a 19: `somar`
```java
return totais.values().stream().mapToInt(Integer::intValue).sum();
```
- `values()`: só os valores (os totais de cada candidato).
- `mapToInt(Integer::intValue)`: converte `Stream<Integer>` em `IntStream`, um stream especializado em `int` que **evita boxing** e oferece `sum()`.
- `sum()`: soma tudo.

**Decisão:** extrair `somar` para um método `private` com nome claro. O método `imprimir` continua legível e cada método faz uma coisa.

---

## 7. `Main.java`

```java
package votacao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        Path arquivo = Path.of(args.length > 0 ? args[0] : "dados/votos.csv");

        List<RegistroVotacao> registros = new LeitorRegistros().ler(arquivo);
        Map<String, Integer> totais = new ConsolidadorVotos().consolidar(registros);

        new RelatorioVotacao().imprimir(totais);
    }
}
```

### Linha 10: `public static void main(String[] args) throws IOException {`
Assinatura obrigatória do ponto de entrada de um programa Java:
- `public`: a JVM precisa acessá-lo de fora.
- `static`: a JVM o chama **sem criar objeto** da classe.
- `void`: não retorna nada.
- `main`: nome fixo reconhecido pela JVM.
- `String[] args`: argumentos passados na linha de comando.
- `throws IOException`: como `ler` pode lançar `IOException`, o `main` a repassa. Se ocorrer, a JVM encerra o programa mostrando o *stack trace*.

### Linha 11: `Path arquivo = Path.of(args.length > 0 ? args[0] : "dados/votos.csv");`
- `Path.of(String)`: fábrica estática (Java 11+) que cria um `Path`.
- Operador **ternário** `condição ? valorSeVerdadeiro : valorSeFalso`: se o usuário passou um argumento, usa `args[0]`; senão, usa o caminho padrão.
- Isso permite executar `java votacao.Main outro.csv` sem recompilar.

### Linhas 13 e 14: montagem do fluxo
```java
List<RegistroVotacao> registros = new LeitorRegistros().ler(arquivo);
Map<String, Integer> totais = new ConsolidadorVotos().consolidar(registros);
```
- `new LeitorRegistros()`: instancia o objeto; `.ler(arquivo)` chama o método na mesma expressão. Como as classes não têm estado (nenhum atributo de instância), não precisamos guardar o objeto em uma variável.
- O fluxo lê-se de cima para baixo como uma receita: **ler → consolidar → imprimir**.
- Cada variável tem nome que revela o conteúdo (`registros`, `totais`).

### Linha 16: `new RelatorioVotacao().imprimir(totais);`
Última etapa: exibe o relatório.

---

## 8. Como compilar e executar

Na raiz do projeto (`votacao/`):

```bash
mkdir -p out
javac --release 17 -d out src/main/java/votacao/*.java
java -cp out votacao.Main dados/votos.csv
```

- `--release 17`: garante compatibilidade com Java 17.
- `-d out`: pasta de destino dos `.class`.
- `-cp out`: *classpath*, onde a JVM procura as classes.
- `votacao.Main`: nome **completo** (pacote + classe).

### Saída esperada

```
RELATÓRIO CONSOLIDADO DE VOTAÇÃO
Ana Souza            245
Bruno Lima           205
Carla Mendes         200
Diego Rocha          165
TOTAL                815
```

Conferência manual: Ana = 120+80+45 = 245; Bruno = 95+110 = 205; Carla = 143+57 = 200; Diego = 64+101 = 165; total = 815.

---

## 9. Resumo das decisões

| Decisão | Motivo |
|---|---|
| `record` para o registro | Imutável, sem código repetitivo, `equals/hashCode` prontos |
| Validação no construtor compacto | *Fail fast*: dado inválido nunca vira objeto |
| Constante `SEPARADOR` | Elimina *magic string* |
| `Map<String, Integer>` declarado pela interface | Flexibilidade para trocar a implementação |
| `HashMap` | Busca O(1), ordem irrelevante na soma |
| `merge(..., Integer::sum)` | Uma operação atômica, sem `if/else` nem dupla busca |
| Ordenar só na exibição | Separa regra de negócio de apresentação |
| `thenComparing` por nome | Desempate determinístico |
| `throws IOException` no leitor | Quem orquestra decide como tratar a falha |
| Uma classe por responsabilidade | Fácil de testar e de evoluir |

## 10. Conceitos de `Map` para fixar

| Método | O que faz |
|---|---|
| `put(k, v)` | Insere ou **substitui** o valor da chave |
| `get(k)` | Retorna o valor ou `null` se não existir |
| `getOrDefault(k, padrao)` | Retorna o valor ou o padrão |
| `containsKey(k)` | Verifica se a chave existe |
| `merge(k, v, fn)` | Insere ou combina com o valor existente |
| `keySet()` | Visão das chaves |
| `values()` | Visão dos valores |
| `entrySet()` | Visão dos pares chave/valor |
| `size()` | Quantidade de pares |
| `remove(k)` | Remove o par da chave |