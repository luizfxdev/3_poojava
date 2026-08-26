# 🏰 O Alojamento de Summoner's Rift — Guia Completo de POO

## O que foi feito?

Criado um **programa que simula uma hospedagem para campeões de League of Legends** com 10 quartos, onde é possível alojar campeões, registrar sua função/rota e exibir o status de todos os quartos. É um exemplo prático de **Programação Orientada a Objetos (POO)** com **Arrays** e **Encapsulamento**.

---

## Conceitos de POO Utilizados

### 1️⃣ **Classe e Atributos**

Criamos **duas classes principais**:

#### Classe `Campeao`
Representa um campeão hospedado com suas características:

```java
public class Campeao {
    private String nome;                // Nome do campeão (ex: Ahri, Yasuo)
    private String funcao;              // Função/Rota (Mid, Top, Jungle, Sup, ADC)
    private int quarto;                 // Número do quarto (0-9)
}
```

**O que significa:**
- `private` = Protege os dados (não podem ser acessados diretamente de fora)
- `String` = Texto (nome e função)
- `int` = Número inteiro (número do quarto)

#### Classe `Hospedagem`
Gerencia todos os 10 quartos e os campeões neles hospedados:

```java
public class Hospedagem {
    private Campeao[] quartos;          // Array com 10 espaços, um para cada quarto
    private static final int TOTAL_QUARTOS = 10;  // Constante: sempre 10 quartos
}
```

**O que significa:**
- `Campeao[]` = Array de Campeões (list de campeões)
- `static final` = Valor constante, não muda nunca

---

### 2️⃣ **Arrays (Vetores)**

Um array é como uma **fileira de caixas**, cada uma com um número:

```
Quarto 0 [ Ahri ]
Quarto 1 [ null ]
Quarto 2 [ Yasuo ]
Quarto 3 [ null ]
Quarto 4 [ Lee Sin ]
...
Quarto 9 [ null ]
```

**Inicializar o array vazio:**
```java
public Hospedagem() {
    this.quartos = new Campeao[TOTAL_QUARTOS];  // Cria array com 10 espaços
    
    for (int i = 0; i < TOTAL_QUARTOS; i++) {
        this.quartos[i] = null;         // Todos começam vazios (null)
    }
}
```

**Explicação:**
- `new Campeao[10]` = Cria 10 espaços
- `for (int i = 0; i < 10; i++)` = Loop que vai de 0 a 9
- `quartos[i] = null` = Marca cada quarto como vazio

---

### 3️⃣ **Construtores**

#### Construtor da classe `Campeao`
Inicializa um novo campeão com seus dados:

```java
public Campeao(String nome, String funcao, int quarto) {
    this.nome = nome;                   // Atribui o nome recebido
    this.funcao = funcao;               // Atribui a função recebida
    this.quarto = quarto;               // Atribui o quarto recebido
}
```

**Uso:**
```java
Campeao campeao = new Campeao("Ahri", "Mid", 3);  // Cria Ahri na rota Mid no quarto 3
```

#### Construtor da classe `Hospedagem`
Inicializa a hospedagem com todos os 10 quartos vazios:

```java
public Hospedagem() {
    this.quartos = new Campeao[TOTAL_QUARTOS];
    for (int i = 0; i < TOTAL_QUARTOS; i++) {
        this.quartos[i] = null;
    }
}
```

**Uso:**
```java
Hospedagem hospedagem = new Hospedagem();  // Cria hospedagem com 10 quartos vazios
```

---

### 4️⃣ **Encapsulamento e Getters**

Protegemos os dados com `private` e criamos getters para acessá-los:

```java
public String getNome() {
    return nome;                        // Retorna o nome (apenas leitura)
}

public String getFuncao() {
    return funcao;                      // Retorna a função (apenas leitura)
}

public int getQuarto() {
    return quarto;                      // Retorna o quarto (apenas leitura)
}
```

**Por que getters e nenhum setter?**
- Os dados do campeão não podem ser alterados após a hospedagem
- Isso garante integridade: o campeão não pode mudar de quarto misteriosamente

---

### 5️⃣ **Métodos com Lógica**

#### **Registrar Campeão**
```java
public void registrarCampeao(String nome, String funcao, int numeroQuarto) {
    // Validação 1: quarto deve estar entre 0 e 9
    if (numeroQuarto < 0 || numeroQuarto >= TOTAL_QUARTOS) {
        System.out.println("❌ Erro: Quarto inválido!");
        return;                         // Sai sem fazer nada
    }

    // Validação 2: quarto já está ocupado?
    if (quartos[numeroQuarto] != null) {
        System.out.printf("❌ Erro: Quarto %d já está ocupado!", numeroQuarto);
        return;                         // Sai sem fazer nada
    }

    // Se passou nas validações, registra o campeão
    Campeao campeao = new Campeao(nome, funcao, numeroQuarto);
    quartos[numeroQuarto] = campeao;    // Coloca o campeão no array

    System.out.printf("✅ %s alocado(a) no quarto %d!", nome, numeroQuarto);
}
```

**O que acontece passo a passo:**
1. Verifica se o número do quarto é válido (0-9)
2. Se inválido, mostra erro e sai
3. Verifica se o quarto já tem alguém
4. Se ocupado, mostra erro e sai
5. Se tudo está ok, cria um novo `Campeao` e coloca no array

#### **Exibir Todos os Quartos**
```java
public void exibirTodos() {
    System.out.println("========== STATUS DOS QUARTOS ==========");
    
    for (int i = 0; i < TOTAL_QUARTOS; i++) {  // Loop por cada quarto
        if (quartos[i] == null) {               // Se vazio
            System.out.printf("Quarto %d: [VAZIO]%n", i);
        } else {                                // Se ocupado
            System.out.printf("Quarto %d: %s (%s)%n", 
                            i, 
                            quartos[i].getNome(),      // Pega nome do campeão
                            quartos[i].getFuncao());   // Pega função do campeão
        }
    }
    System.out.println("========================================");
}
```

**O que acontece:**
- Loop varre todos os 10 quartos (0 a 9)
- Para cada quarto, verifica se está vazio ou ocupado
- Se vazio, mostra `[VAZIO]`
- Se ocupado, mostra nome e função usando `getNome()` e `getFuncao()`

#### **Contar Quartos Ocupados**
```java
public int contarOcupados() {
    int contador = 0;                   // Começa com 0
    
    for (int i = 0; i < TOTAL_QUARTOS; i++) {
        if (quartos[i] != null) {       // Se quarto não é vazio
            contador++;                 // Incrementa
        }
    }
    
    return contador;                    // Retorna total
}
```

**O que acontece:**
- Loop varre todos os 10 quartos
- Cada vez que encontra um quarto ocupado (`!= null`), soma +1 no contador
- Retorna o total de quartos ocupados

---

## Exemplo Prático Passo a Passo

```java
public class Main {
    public static void main(String[] args) {
        // Passo 1: Criar hospedagem com 10 quartos vazios
        Hospedagem hospedagem = new Hospedagem();
        
        // Passo 2: Registrar primeiro campeão
        hospedagem.registrarCampeao("Ahri", "Mid", 0);
        // Resultado: ✅ Ahri alocado(a) no quarto 0 (Mid)
        
        // Passo 3: Registrar segundo campeão
        hospedagem.registrarCampeao("Lee Sin", "Jungle", 2);
        // Resultado: ✅ Lee Sin alocado(a) no quarto 2 (Jungle)
        
        // Passo 4: Tentar alojar no quarto já ocupado
        hospedagem.registrarCampeao("Yasuo", "Mid", 0);
        // Resultado: ❌ Erro: Quarto 0 já está ocupado por Ahri
        
        // Passo 5: Tentar alojar em quarto inválido
        hospedagem.registrarCampeao("Garen", "Top", 15);
        // Resultado: ❌ Erro: Quarto inválido! Escolha entre 0 e 9.
        
        // Passo 6: Exibir status de todos os quartos
        hospedagem.exibirTodos();
        // Resultado:
        // ========== STATUS DOS QUARTOS ==========
        // Quarto 0: Ahri (Mid)
        // Quarto 1: [VAZIO]
        // Quarto 2: Lee Sin (Jungle)
        // Quarto 3: [VAZIO]
        // Quarto 4: [VAZIO]
        // ... (mais vazios)
        // ========================================
        
        // Passo 7: Contar ocupados
        int ocupados = hospedagem.contarOcupados();
        System.out.printf("Total: %d/10%n", ocupados);
        // Resultado: Total: 2/10
    }
}
```

---

## Resumo das Decisões Importantes

| Decisão | Por quê? |
|---------|----------|
| **Array de Campeão[]** | Perfeito para 10 quartos numerados de 0 a 9 |
| **Atributos com `private`** | Ninguém altera dados do campeão após criação |
| **Getters sem setters** | Campeões não mudam de quarto após registro |
| **Validação de quarto** | Impede alojar em quarto inválido ou ocupado |
| **`quartos[i] = null`** | Forma padrão em Java para marcar posição vazia |
| **Loop for para inicializar** | Garante que todos os 10 quartos começam vazios |
| **Método `contarOcupados()`** | Útil para saber quantos quartos estão em uso |

---

## Conceitos-Chave para Fixação

✅ **Array** = Lista ordenada com índices (0, 1, 2, ..., 9)

✅ **Null** = Valor que representa "vazio" em Java

✅ **Classe** = Template de um objeto real (Campeao e Hospedagem)

✅ **Atributos** = Características (nome, funcao, quarto, quartos[])

✅ **Construtores** = Inicializam objetos novos

✅ **Encapsulamento** = Proteger dados com `private` e expor via getters

✅ **Métodos** = Ações da classe (registrarCampeao, exibirTodos, contarOcupados)

✅ **Validação** = Verificar se dados são válidos antes de processar

✅ **This** = Referência ao próprio objeto (`this.nome`, `this.quartos`)

✅ **Static Final** = Constante que não muda (TOTAL_QUARTOS)

---

## Analogia com League of Legends 🎮

Pense na hospedagem como um **Nexus**:
- **Array de quartos** = Estrutura do Nexus (10 salas)
- **Cada campeão** = Um defensor do Nexus
- **Encapsulamento** = Um campeão não pode estar em dois lugares ao mesmo tempo
- **Validação** = Não permite campeão se houver inimigo no quarto
- **Null** = Quarto vazio esperando por um defensor

---

## Como Executar

**Compilar:**
```bash
javac Campeao.java Hospedagem.java Main.java
```

**Executar:**
```bash
java Main
```

**Regra de Negócio:**
1. Programa pergunta: "Quantos campeões serão alojados? (1-10):"
2. Para cada campeão, pede: Nome, Função, Quarto
3. Valida cada entrada
4. Exibe status final de todos os quartos
5. Mostra total de quartos ocupados