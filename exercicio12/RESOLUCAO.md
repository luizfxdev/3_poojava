# Resolução do Exercício — Passo a Passo

Documento de raciocínio: o que o enunciado pede, quais são as regras de negócio, como cada regra virou código e por que cada decisão foi tomada.

---

## 1. Leitura do enunciado

> Uma empresa deseja automatizar o processamento de seus contratos. O processamento de um contrato consiste em gerar as parcelas a serem pagas para aquele contrato, com base no número de meses desejado.

Extraindo os fatos:

| Fato do enunciado | Consequência de projeto |
|---|---|
| "gerar as parcelas **para aquele contrato**" | Parcela pertence ao contrato → composição, não associação solta |
| "com base no número de meses desejado" | A quantidade de parcelas é entrada do processamento, **não** atributo do contrato |
| "serviço de pagamento online" (sublinhado no enunciado) | É um ponto de variação → vira interface |
| "Por enquanto, o serviço contratado é o do Paypal" | "Por enquanto" = vai mudar → confirma a interface |
| "juros simples de 1% a cada parcela" | Juro proporcional ao número de meses, sem capitalização |
| "mais uma taxa de pagamento de 2%" | Segunda cobrança, aplicada depois do juro |
| "primeira parcela um mês após a data do contrato" | Vencimento = data do contrato + i meses, com i começando em 1 |

O termo "por enquanto" é o que justifica toda a arquitetura. Sem ele, `ServicoDePagamentoOnline` seria abstração desnecessária.

---

## 2. As regras de negócio, isoladas

### RN01 — Valor base da parcela
```
valorBase = valorTotal / quantidadeDeParcelas
```
No exemplo: `600.00 / 3 = 200.00`.

### RN02 — Juro simples proporcional ao mês
```
juros(i) = valorBase × 0.01 × i        (i = 1, 2, ..., N)
```
Simples, não composto: a base é sempre `valorBase`, nunca o valor já acrescido.

| Parcela | Juro simples (correto) | Juro composto (errado aqui) |
|---|---|---|
| 1 | 200 + 2,00 = 202,00 | 200 × 1,01 = 202,00 |
| 2 | 200 + 4,00 = 204,00 | 202 × 1,01 = 204,02 |
| 3 | 200 + 6,00 = 206,00 | 204,02 × 1,01 = 206,06 |

A partir da segunda parcela os dois divergem. O enunciado e o exemplo pedem a coluna do meio.

### RN03 — Taxa de pagamento sobre o valor já acrescido de juros
```
valorComJuros(i) = valorBase + juros(i)
taxa(i)          = valorComJuros(i) × 0.02
valorFinal(i)    = valorComJuros(i) + taxa(i)
```

A ordem importa. O exemplo mostra `202 + 2% = 206.04`, ou seja, a taxa incide sobre 202 e não sobre 200:

| Ordem | Cálculo | Resultado |
|---|---|---|
| Juro depois taxa (correto) | (200 + 2) × 1,02 | **206,04** |
| Taxa depois juro (errado) | (200 × 1,02) + 2 | 206,00 |

Quatro centavos de diferença na primeira parcela — e o erro cresce com o número de parcelas.

### RN04 — Vencimento
```
vencimento(i) = dataDoContrato.plusMonths(i)
```
Com `i` começando em 1: `25/06/2018 + 1 mês = 25/07/2018`.

### RN05 — Validações
- Número do contrato > 0
- Valor total > 0
- Quantidade de parcelas > 0
- Valor de parcela gerado > 0
- Data no formato `dd/MM/yyyy`, estritamente válida

---

## 3. Conferindo o exemplo na mão

Entrada: contrato 8028, 25/06/2018, R$ 600,00, 3 parcelas.

`valorBase = 600 / 3 = 200,00`

| i | juros = 200 × 1% × i | comJuros | taxa = comJuros × 2% | valorFinal | vencimento |
|---|---|---|---|---|---|
| 1 | 2,00 | 202,00 | 4,04 | **206,04** | 25/07/2018 |
| 2 | 4,00 | 204,00 | 4,08 | **208,08** | 25/08/2018 |
| 3 | 6,00 | 206,00 | 4,12 | **210,12** | 25/09/2018 |

Confere com o enunciado. Esse é o caso que virou o primeiro teste automatizado.

---

## 4. Modelagem

### Passo 4.1 — `Parcela`

Uma parcela é um valor numa data. Não tem identidade, não tem comportamento, nunca muda depois de criada. É um *value object* — em Java 17, um `record`.

```java
public record Parcela(LocalDate vencimento, BigDecimal valor) {
    public Parcela {
        Objects.requireNonNull(vencimento, "O vencimento da parcela e obrigatorio.");
        Objects.requireNonNull(valor, "O valor da parcela e obrigatorio.");
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor da parcela deve ser maior que zero.");
        }
    }
}
```

O construtor compacto valida na criação: não existe `Parcela` inválida em memória.

### Passo 4.2 — `Contrato`

Agregado raiz. Atributos finais, parcelas encapsuladas:

```java
public List<Parcela> getParcelas() {
    return Collections.unmodifiableList(parcelas);
}
```

Sem isso, qualquer classe poderia fazer `contrato.getParcelas().clear()`. A lista é do contrato, e quem quiser alterá-la passa por `adicionarParcela` ou `limparParcelas`.

### Passo 4.3 — Por que `BigDecimal` e não `double`

```java
System.out.println(0.1 + 0.2);      // 0.30000000000000004
System.out.println(200 * 0.01);     // 2.0000000000000004
```

`double` é binário; `0.01` não tem representação exata em binário. Em 3 parcelas o erro é invisível; em um relatório de 10 mil parcelas ele vira divergência contábil. Dinheiro é `BigDecimal` com escala e arredondamento explícitos — ou `long` em centavos.

---

## 5. A interface do serviço de pagamento

O ponto central do exercício. O enunciado diz "por enquanto, o serviço é o do Paypal" — logo o processamento não pode depender do Paypal.

```java
public interface ServicoDePagamentoOnline {
    BigDecimal calcularJuros(BigDecimal valorBase, int quantidadeDeMeses);
    BigDecimal calcularTaxaDePagamento(BigDecimal valorComJuros);
    String nome();
}
```

Escolha de assinatura: os métodos retornam **o acréscimo**, não o total.

| Alternativa | Problema |
|---|---|
| `aplicarJuros(valor, meses)` retornando o total | O chamador não consegue saber quanto foi de juro — impossível detalhar no extrato |
| `calcularJuros(...)` retornando só o juro | O processador compõe; o serviço fica uma calculadora pura |

Retornar o acréscimo mantém o serviço sem estado e sem conhecimento da ordem de aplicação — a ordem (RN03) é regra do processamento, não do gateway.

A implementação carrega as alíquotas como constantes, com um construtor alternativo para testes e cenários de negociação:

```java
public ServicoDePagamentoPaypal() {
    this(JURO_MENSAL_PADRAO, TAXA_DE_PAGAMENTO_PADRAO);
}

public ServicoDePagamentoPaypal(BigDecimal juroMensal, BigDecimal taxaDePagamento) { ... }
```

`new BigDecimal("0.01")` e nunca `new BigDecimal(0.01)`: o construtor que recebe `double` produz `0.01000000000000000020816681711721685...`.

---

## 6. O processador

```java
public void processar(Contrato contrato, int quantidadeDeParcelas) {
    Objects.requireNonNull(contrato, "O contrato e obrigatorio.");
    if (quantidadeDeParcelas <= 0) {
        throw new IllegalArgumentException("A quantidade de parcelas deve ser maior que zero.");
    }

    contrato.limparParcelas();

    BigDecimal valorBase = calcularValorBase(contrato.getValorTotal(), quantidadeDeParcelas);

    for (int mes = 1; mes <= quantidadeDeParcelas; mes++) {
        BigDecimal juros = servicoDePagamento.calcularJuros(valorBase, mes);
        BigDecimal valorComJuros = valorBase.add(juros);
        BigDecimal taxa = servicoDePagamento.calcularTaxaDePagamento(valorComJuros);
        BigDecimal valorFinal = valorComJuros.add(taxa).setScale(ESCALA_MONETARIA, ARREDONDAMENTO);
        LocalDate vencimento = contrato.getData().plusMonths(mes);
        contrato.adicionarParcela(new Parcela(vencimento, valorFinal));
    }
}
```

Quatro detalhes que carregam regra de negócio:

**`limparParcelas()` no início.** Sem isso, processar o mesmo contrato duas vezes acumularia parcelas das duas execuções. O processamento é idempotente: mesma entrada, mesmo resultado.

**`mes` começando em 1.** É simultaneamente o multiplicador do juro (RN02) e o deslocamento do vencimento (RN04). Começar em 0 daria juro zero na primeira parcela e vencimento na própria data do contrato.

**Arredondamento só no fim.** A divisão usa escala 10 (`ESCALA_DE_CALCULO`); o arredondamento para 2 casas acontece uma única vez, no valor final. Arredondar a cada etapa acumularia erro:

| Estratégia para 1000,00 em 7x | Primeira parcela |
|---|---|
| Arredondar `valorBase` para 142,86 e depois calcular | 147,17 (com erro embutido de 0,003 por parcela) |
| Manter 142,8571428571 e arredondar só no fim | 147,17 (exato) |

Com 7 parcelas a diferença ainda não aparece nos centavos; com valores maiores, aparece.

**`RoundingMode.HALF_UP`.** Convenção financeira usual no Brasil (0,005 sobe). `HALF_EVEN` seria defensável para grandes volumes estatísticos; o que não se aceita é deixar implícito.

---

## 7. Entrada e saída

Ambas são detalhes — por isso estão atrás de interfaces e fora da regra de negócio.

### Data com `ResolverStyle.STRICT`

```java
DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
```

Dois pontos:

- `uuuu` e não `yyyy`: no modo estrito, `yyyy` (year-of-era) exige a era e falha; `uuuu` é o ano proléptico.
- Sem `STRICT`, `31/02/2018` é silenciosamente convertido para `28/02/2018`. Com `STRICT`, vira erro.

### Formatação monetária

```java
String.format(Locale.US, "%s - %.2f", ...)
```

`Locale.US` fixa o ponto decimal. Sem ele, a mesma aplicação imprime `206.04` em um servidor e `206,04` em outro — diferença que quebra parsers a jusante.

### Erros de entrada

`EntradaInvalidaException` separa dois mundos: erro do usuário (digitou `abc` no valor) de erro de programação (`NullPointerException`). `Principal` captura o primeiro e imprime mensagem legível com código de saída 1.

---

## 8. Montagem em `Principal`

```java
LeitorDeDadosDoContrato leitor = new LeitorDeDadosDoContratoConsole(scanner, System.out);
ServicoDePagamentoOnline servicoDePagamento = new ServicoDePagamentoPaypal();
ProcessadorDeContrato processador = new ProcessadorDeContratoPadrao(servicoDePagamento);
ApresentadorDeParcelas apresentador = new ApresentadorDeParcelasConsole(System.out);
```

Este é o único lugar do sistema onde classes concretas são instanciadas — o *composition root*. Todo o resto programa contra interfaces. Trocar o gateway é alterar uma linha aqui.

O `Scanner` fica em try-with-resources e é injetado no leitor, em vez de criado dentro dele: quem cria o recurso é quem fecha.

---

## 9. Casos de borda tratados

| Caso | Comportamento |
|---|---|
| Contrato em 31/01 parcelado | `plusMonths` ajusta para 28/02 (ou 29/02 em ano bissexto) — o dia não existe no mês seguinte |
| Valor que não divide exato (1000 em 7x) | Precisão de 10 casas no cálculo, arredondamento único no fim |
| Reprocessamento do mesmo contrato | Parcelas anteriores descartadas |
| 0 ou número negativo de parcelas | `IllegalArgumentException` com mensagem clara |
| Data inexistente (31/02/2018) | `EntradaInvalidaException` |
| Valor digitado com vírgula (`600,00`) | Normalizado para ponto antes do parse |
| Campo vazio ou entrada encerrada | `EntradaInvalidaException` nomeando o campo |

Ponto em aberto e consciente: a soma das parcelas não é igual ao valor do contrato — nem deveria ser, já que juro e taxa são acréscimos. Quando a divisão não é exata, pode sobrar ou faltar centavo na soma dos arredondamentos. Se o negócio exigir que a soma feche num valor alvo, a correção é distribuir o resíduo na última parcela — e isso é uma nova implementação de `ProcessadorDeContrato`, não uma alteração nesta.

---

## 10. Testes

O teste principal é o exemplo do enunciado, transformado em asserção:

```java
Contrato contrato = new Contrato(8028, LocalDate.of(2018, 6, 25), new BigDecimal("600.00"));
processador.processar(contrato, 3);

assertEquals(new BigDecimal("206.04"), parcelas.get(0).valor());
assertEquals(new BigDecimal("208.08"), parcelas.get(1).valor());
assertEquals(new BigDecimal("210.12"), parcelas.get(2).valor());
```

`assertEquals` direto no valor final (compara valor **e** escala, garantindo 2 casas) e `compareTo` nos testes do serviço, onde só o valor numérico importa.

Os demais testes cobrem os casos de borda da seção 9. A regra de negócio é testável sem console porque o processador não conhece `System.in` nem `System.out`.

---

## 11. Execução

```bash
gradle run
```

Entrada:
```
8028
25/06/2018
600.00
3
```

Saída obtida:
```
Entre os dados do contrato:
Numero: Data (dd/MM/yyyy): Valor do contrato: Entre com o numero de parcelas: Parcelas:
25/07/2018 - 206.04
25/08/2018 - 208.08
25/09/2018 - 210.12
```

Idêntica ao enunciado.

---

## 12. Resumo das decisões

| # | Decisão | Alternativa descartada | Motivo |
|---|---|---|---|
| 1 | `ServicoDePagamentoOnline` como interface | Classe `Paypal` concreta | O enunciado anuncia a troca de provedor |
| 2 | Serviço retorna o acréscimo | Retornar o total | Permite detalhar juro e taxa separadamente |
| 3 | Ordem juro → taxa | Taxa → juro | O exemplo do enunciado determina |
| 4 | `BigDecimal` | `double` | Precisão monetária |
| 5 | Arredondar só no valor final | Arredondar a cada etapa | Evita acúmulo de erro |
| 6 | Quantidade de parcelas como parâmetro | Atributo do contrato | O enunciado a lê depois do contrato |
| 7 | `limparParcelas()` antes de gerar | Acumular | Processamento idempotente |
| 8 | `record` para `Parcela` | Classe com setters | Imutabilidade de value object |
| 9 | Injeção por construtor | `new` dentro da classe | Testabilidade e baixo acoplamento |
| 10 | Interfaces também para E/S | Console direto no processador | Permite REST, arquivo ou CSV sem tocar na regra |