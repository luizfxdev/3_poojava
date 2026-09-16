# Resolução - Endereçamento de Pallets

## Enunciado

Fazer um programa para ler os dados de um endereço de pallet em um armazém (número do endereço, código do pallet, quantidade inicial e capacidade máxima) e depois realizar armazenamentos e retiradas neste endereço, mostrando a nova quantidade. Um armazenamento não pode ocorrer se não houver espaço disponível no endereço, ou se a quantidade a armazenar for superior à capacidade restante. Uma retirada não pode ocorrer se não houver quantidade disponível no endereço. Implemente o endereço de pallet conforme projeto abaixo:

**Diagrama UML:**
- EndorecoPallet
  - numero: Integer
  - codigoPallet: String
  - quantidade: Double
  - capacidadeMaxima: Double
  - +armazenar(valor: Double): void
  - +retirar(valor: Double): void

---

## Resolução Linha por Linha

### 1. Classe CapacidadeExcedidaException

```java
public class CapacidadeExcedidaException extends Exception {
    public CapacidadeExcedidaException(String mensagem) {
        super(mensagem);
    }
}
```

- **`extends Exception`**: Herda de Exception (exceção checked). Obrigatório capturar com try-catch ou declarar com throws.
- **Construtor**: Recebe mensagem de erro e passa para classe pai via `super(mensagem)`.
- Lançada quando armazenamento excederia a capacidade máxima.

---

### 2. Classe QuantidadeInsuficienteException

```java
public class QuantidadeInsuficienteException extends Exception {
    public QuantidadeInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
```

- **`extends Exception`**: Herda de Exception (exceção checked).
- Lançada quando tentativa de retirada excede quantidade disponível.

---

### 3. Classe EndorecoPallet - Atributos e Construtor

```java
public class EndorecoPallet {
    private int numero;
    private String codigoPallet;
    private double quantidade;
    private double capacidadeMaxima;
    
    public EndorecoPallet(int numero, String codigoPallet, double quantidade, double capacidadeMaxima) 
            throws CapacidadeExcedidaException {
        validar(quantidade, capacidadeMaxima);
        this.numero = numero;
        this.codigoPallet = codigoPallet;
        this.quantidade = quantidade;
        this.capacidadeMaxima = capacidadeMaxima;
    }
}
```

- **Atributos privados**: Encapsulamento de dados. Não pode acessar diretamente de fora.
- **numero**: Identificador único do endereço no armazém.
- **codigoPallet**: Código ou identificação do pallet armazenado.
- **quantidade**: Quantidade atual de itens/peso no endereço.
- **capacidadeMaxima**: Limite máximo que o endereço pode armazenar.
- **`throws CapacidadeExcedidaException`**: Construtor pode lançar exceção. Obriga quem chamar a tratar.
- **`validar(quantidade, capacidadeMaxima)`**: Valida dados antes de atribuir.

---

### 4. Classe EndorecoPallet - Método validar()

```java
private void validar(double quantidade, double capacidadeMaxima) 
        throws CapacidadeExcedidaException {
    if (quantidade < 0) {
        throw new CapacidadeExcedidaException("Quantidade não pode ser negativa");
    }
    if (quantidade > capacidadeMaxima) {
        throw new CapacidadeExcedidaException(
            "Quantidade excede a capacidade máxima do endereço. Capacidade: " + capacidadeMaxima
        );
    }
}
```

- **`quantidade < 0`**: Valida se quantidade inicial é negativa.
- **`quantidade > capacidadeMaxima`**: Valida se quantidade inicial excede limite.
- **`throw new CapacidadeExcedidaException(...)`**: Se validação falhar, lança exceção com mensagem descritiva.

---

### 5. Classe EndorecoPallet - Método armazenar()

```java
public void armazenar(double valor) throws CapacidadeExcedidaException {
    double novaQuantidade = this.quantidade + valor;
    validarCapacidade(novaQuantidade);
    this.quantidade = novaQuantidade;
}
```

- **`novaQuantidade`**: Calcula quantidade após armazenamento.
- **`validarCapacidade(novaQuantidade)`**: Verifica se nova quantidade não excede capacidade.
- Se validação passar, atualiza atributo quantidade.
- **Ordem importa**: Valida ANTES de modificar estado do objeto.

---

### 6. Classe EndorecoPallet - Método validarCapacidade()

```java
private void validarCapacidade(double novaQuantidade) throws CapacidadeExcedidaException {
    if (novaQuantidade > this.capacidadeMaxima) {
        throw new CapacidadeExcedidaException(
            "Armazenamento excederia a capacidade. Espaço disponível: " + 
            (this.capacidadeMaxima - this.quantidade)
        );
    }
}
```

- **`novaQuantidade > this.capacidadeMaxima`**: Se nova quantidade ultrapassa limite.
- Mensagem inclui espaço disponível para facilitar decisão do usuário.

---

### 7. Classe EndorecoPallet - Método retirar()

```java
public void retirar(double valor) throws QuantidadeInsuficienteException {
    if (valor > this.quantidade) {
        throw new QuantidadeInsuficienteException(
            "Quantidade insuficiente no endereço. Disponível: " + this.quantidade
        );
    }
    this.quantidade -= valor;
}
```

- **`valor > this.quantidade`**: Se tentativa de retirada excede disponível.
- Mensagem informa quantidade disponível no endereço.
- **`this.quantidade -= valor`**: Se validação passa, reduz quantidade.

---

### 8. Classe EndorecoPallet - Métodos auxiliares

```java
public double obterEspacoDisponivel() {
    return capacidadeMaxima - quantidade;
}

public double obterTaxaOcupacao() {
    return (quantidade / capacidadeMaxima) * 100;
}
```

- **`obterEspacoDisponivel()`**: Calcula espaço livre no endereço.
- **`obterTaxaOcupacao()`**: Calcula percentual de ocupação.
- **Good practice**: Métodos com responsabilidade única (Single Responsibility Principle).

---

### 9. Classe EndorecoPallet - toString()

```java
@Override
public String toString() {
    return String.format(
        "Endereço: %d%nCódigo do Pallet: %s%nQuantidade: %.2f%nCapacidade Máxima: %.2f%nEspaço Disponível: %.2f%nTaxa de Ocupação: %.2f%%",
        numero, codigoPallet, quantidade, capacidadeMaxima, obterEspacoDisponivel(), obterTaxaOcupacao()
    );
}
```

- **`@Override`**: Sobrescreve método toString() de Object.
- **`String.format(...)`**: Formata string com valores interpolados.
- **`%d`**: Integer (numero).
- **`%s`**: String (codigoPallet).
- **`%.2f`**: Double com 2 casas decimais.
- **`%n`**: Quebra de linha (newline).

---

### 10. Classe Principal - Bloco try

```java
Scanner sc = new Scanner(System.in);

try {
    System.out.print("Número do endereço: ");
    int numero = sc.nextInt();
    sc.nextLine();
    
    System.out.print("Código do pallet: ");
    String codigoPallet = sc.nextLine();
    
    System.out.print("Quantidade inicial: ");
    double quantidade = sc.nextDouble();
    
    System.out.print("Capacidade máxima do endereço: ");
    double capacidadeMaxima = sc.nextDouble();
    
    EndorecoPallet endereco = new EndorecoPallet(numero, codigoPallet, quantidade, capacidadeMaxima);
```

- **`sc.nextInt()`**: Lê inteiro.
- **`sc.nextLine()`**: Consome quebra de linha deixada por nextInt().
- **`sc.nextDouble()`**: Lê double.
- **Construtor**: Se lançar exceção, pula para catch.

---

### 11. Classe Principal - Primeira operação

```java
    System.out.println("\nDados do endereço:");
    System.out.println(endereco);
    
    System.out.print("\nQuantidade a armazenar: ");
    double armazenar = sc.nextDouble();
    endereco.armazenar(armazenar);
    System.out.println("Armazenamento realizado com sucesso!");
    
    System.out.println("\nDados do endereço (atualizado):");
    System.out.println(endereco);
```

- **`System.out.println(endereco)`**: Chama toString() automaticamente.
- **`endereco.armazenar(armazenar)`**: Se lançar exceção, pula para catch.
- Só exibe "com sucesso" se operação passar sem exceção.

---

### 12. Classe Principal - Segunda operação

```java
    System.out.print("\nQuantidade a retirar: ");
    double retirar = sc.nextDouble();
    endereco.retirar(retirar);
    System.out.println("Retirada realizada com sucesso!");
    
    System.out.println("\nDados do endereço (atualizado):");
    System.out.println(endereco);
```

- Mesma lógica de armazenamento.
- **`endereco.retirar(retirar)`**: Se lançar exceção, pula para catch.

---

### 13. Classe Principal - Tratamento de exceções

```java
} catch (CapacidadeExcedidaException e) {
    System.out.println("ERRO: " + e.getMessage());
} catch (QuantidadeInsuficienteException e) {
    System.out.println("ERRO: " + e.getMessage());
} catch (Exception e) {
    System.out.println("ERRO: " + e.getMessage());
} finally {
    sc.close();
}
```

- **`catch (CapacidadeExcedidaException e)`**: Captura especificamente CapacidadeExcedidaException.
- **`catch (QuantidadeInsuficienteException e)`**: Captura especificamente QuantidadeInsuficienteException.
- **`catch (Exception e)`**: Captura qualquer outra exceção (ex: formato inválido).
- **`finally`**: **Sempre executa**, mesmo com exceção. Fecha Scanner para evitar vazamento de recurso.
- **Ordem importa**: Mais específico antes, genérico por último.
- **`e.getMessage()`**: Retorna mensagem passada no construtor da exceção.

---

## Fluxo Esperado - Sucesso

```
Número do endereço: 101
Código do pallet: PAL-A001
Quantidade inicial: 50
Capacidade máxima do endereço: 100

Dados do endereço:
Endereço: 101
Código do Pallet: PAL-A001
Quantidade: 50.00
Capacidade Máxima: 100.00
Espaço Disponível: 50.00
Taxa de Ocupação: 50.00%

Quantidade a armazenar: 30
Armazenamento realizado com sucesso!

Dados do endereço (atualizado):
Endereço: 101
Código do Pallet: PAL-A001
Quantidade: 80.00
Capacidade Máxima: 100.00
Espaço Disponível: 20.00
Taxa de Ocupação: 80.00%

Quantidade a retirar: 20
Retirada realizada com sucesso!

Dados do endereço (atualizado):
Endereço: 101
Código do Pallet: PAL-A001
Quantidade: 60.00
Capacidade Máxima: 100.00
Espaço Disponível: 40.00
Taxa de Ocupação: 60.00%
```

---

## Fluxo Esperado - Erro (Capacidade Excedida)

```
Número do endereço: 101
Código do pallet: PAL-A001
Quantidade inicial: 50
Capacidade máxima do endereço: 100

Dados do endereço:
Endereço: 101
Código do Pallet: PAL-A001
Quantidade: 50.00
Capacidade Máxima: 100.00
Espaço Disponível: 50.00
Taxa de Ocupação: 50.00%

Quantidade a armazenar: 60
ERRO: Armazenamento excederia a capacidade. Espaço disponível: 50.00
```

---

## Fluxo Esperado - Erro (Quantidade Insuficiente)

```
Número do endereço: 101
Código do pallet: PAL-A001
Quantidade inicial: 30
Capacidade máxima do endereço: 100

Dados do endereço:
Endereço: 101
Código do Pallet: PAL-A001
Quantidade: 30.00
Capacidade Máxima: 100.00
Espaço Disponível: 70.00
Taxa de Ocupação: 30.00%

Quantidade a armazenar: 20
Armazenamento realizado com sucesso!

Dados do endereço (atualizado):
Endereço: 101
Código do Pallet: PAL-A001
Quantidade: 50.00
Capacidade Máxima: 100.00
Espaço Disponível: 50.00
Taxa de Ocupação: 50.00%

Quantidade a retirar: 60
ERRO: Quantidade insuficiente no endereço. Disponível: 50.00
```

---

## Conceitos-Chave Aplicados

| Conceito | Aplicação |
|----------|-----------|
| **Encapsulamento** | Atributos privados + métodos públicos |
| **Validação** | Método validar() antes de atribuir valores |
| **Exceções Checked** | Obrigatório capturar ou declarar com throws |
| **Single Responsibility** | Cada método tem responsabilidade única |
| **toString()** | Representação em string para debug e exibição |
| **Try-Catch-Finally** | Tratamento de erros e liberação de recursos |
| **Mensagens Descritivas** | Exceções informam o que deu errado e estado atual |