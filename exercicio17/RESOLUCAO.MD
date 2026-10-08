# Resolução — Exercício: Cadastro de Entregas

---

## Classe `Entrega`

```java
public class Entrega {
```

- `public` → visível em qualquer pacote.
- Classe concreta (não abstrata), pois será instanciada diretamente.

---

## Atributos

```java
private final String cliente;
private final String cidade;
private double pesoKg;
private double frete;
```

| Atributo  | Tipo     | Modificadores   | Decisão |
|-----------|----------|-----------------|---------|
| `cliente` | `String` | `private final` | Sem setter; `final` impede reatribuição após construtor |
| `cidade`  | `String` | `private final` | Idem |
| `pesoKg`  | `double` | `private`       | Mutável (tem setter com validação) |
| `frete`   | `double` | `private`       | Mutável (tem setter com validação) |

`private` em todos: encapsulamento — o estado interno só é acessado via getters/setters.

---

## Construtor

```java
public Entrega(String cliente, String cidade, double pesoKg, double frete) {
    if (pesoKg <= 0) throw new IllegalArgumentException("pesoKg deve ser maior que zero.");
    if (frete < 0) throw new IllegalArgumentException("frete não pode ser negativo.");
    this.cliente = cliente;
    this.cidade = cidade;
    this.pesoKg = pesoKg;
    this.frete = frete;
}
```

- **Validação antes da atribuição**: garante que o objeto nunca existirá em estado inválido.
- `IllegalArgumentException` é unchecked (herda de `RuntimeException`), portanto não precisa de `throws` na assinatura.
- `this.campo = parametro` distingue o atributo da instância do parâmetro local de mesmo nome.
- Como `cliente` e `cidade` são `final`, só podem ser atribuídos aqui. Tentar atribuí-los em um setter causaria erro de compilação.

---

## Getters

```java
public String getCliente() { return cliente; }
public String getCidade() { return cidade; }
public double getPesoKg() { return pesoKg; }
public double getFrete() { return frete; }
```

- Padrão JavaBeans: prefixo `get` + nome do atributo com inicial maiúscula.
- Expõem leitura sem expor o atributo diretamente.
- Necessários para o `Comparator` e `mapToDouble` via **method reference** (`Entrega::getFrete`).

---

## Setters (apenas para campos mutáveis)

```java
public void setPesoKg(double pesoKg) {
    if (pesoKg <= 0) throw new IllegalArgumentException("pesoKg deve ser maior que zero.");
    this.pesoKg = pesoKg;
}

public void setFrete(double frete) {
    if (frete < 0) throw new IllegalArgumentException("frete não pode ser negativo.");
    this.frete = frete;
}
```

- A validação é repetida aqui porque o setter é um ponto de entrada independente do construtor.
- `cliente` e `cidade` **não têm setter** — requisito explícito do enunciado.

---

## `toString`

```java
@Override
public String toString() {
    return "Entrega{cliente='%s', cidade='%s', pesoKg=%.2f, frete=R$%.2f}"
            .formatted(cliente, cidade, pesoKg, frete);
}
```

- `@Override` instrui o compilador a verificar que realmente existe um método com essa assinatura na superclasse (`Object`). Se houver erro de digitação no nome, o compilador avisa.
- `String.formatted(...)` (Java 15+): alternativa mais legível ao `String.format(...)` estático.
- `%.2f` formata `double` com 2 casas decimais.
- Usado implicitamente pelo `System.out::println` no `forEach`.

---

## `main` — Lista de entregas

```java
List<Entrega> entregas = new ArrayList<>(List.of(...));
```

- `List.of(...)` cria uma lista **imutável** com os 6 elementos.
- `new ArrayList<>(...)` envolve essa lista em uma `ArrayList` **mutável**, necessária para `.sort(...)`.
- Sem isso, `sort` lançaria `UnsupportedOperationException`.

---

## Ordenação por frete decrescente

```java
entregas.sort(Comparator.comparingDouble(Entrega::getFrete).reversed());
```

| Parte | O que faz |
|-------|-----------|
| `Comparator.comparingDouble(...)` | Cria um `Comparator<Entrega>` que compara pelo `double` retornado pela função |
| `Entrega::getFrete` | Method reference — equivale a `e -> e.getFrete()` |
| `.reversed()` | Inverte a ordem natural (crescente → decrescente) |

- `comparingDouble` existe especificamente para `double` e evita o boxing/unboxing que `comparing` com `Double` causaria.

---

## Impressão com `forEach` e method reference

```java
entregas.forEach(System.out::println);
```

- `forEach` recebe um `Consumer<Entrega>` — uma função que recebe um elemento e não retorna nada.
- `System.out::println` é uma **instance method reference** sobre o objeto `System.out`. Equivale a `e -> System.out.println(e)`.
- `println` chama `toString()` automaticamente quando recebe um `Object`.

---

## Frete total com `mapToDouble`

```java
double freteTotal = entregas.stream()
        .mapToDouble(Entrega::getFrete)
        .sum();
```

| Parte | O que faz |
|-------|-----------|
| `.stream()` | Cria um `Stream<Entrega>` a partir da lista |
| `.mapToDouble(Entrega::getFrete)` | Transforma cada `Entrega` no `double` do seu frete, produzindo um `DoubleStream` |
| `.sum()` | Operação terminal — reduz o `DoubleStream` a um único `double` |

- `mapToDouble` é preferível a `map` + `reduce` para somas de primitivos: evita boxing e é mais legível.
- `sum()` é uma operação terminal, o que significa que o stream é consumido após essa chamada.

---

## Saída esperada

```
Entrega{cliente='Carlos Melo', cidade='Rio de Janeiro', pesoKg=10,00, frete=R$80,00}
Entrega{cliente='Fernanda Cruz', cidade='Recife', pesoKg=8,30, frete=R$70,00}
Entrega{cliente='Diego Ramos', cidade='Curitiba', pesoKg=5,00, frete=R$55,00}
Entrega{cliente='Gabriel Neves', cidade='Salvador', pesoKg=3,70, frete=R$45,00}
Entrega{cliente='Ana Lima', cidade='São Paulo', pesoKg=2,50, frete=R$35,00}
Entrega{cliente='Beatriz Souza', cidade='Belo Horizonte', pesoKg=1,20, frete=R$20,00}

Frete total: R$305,00
```