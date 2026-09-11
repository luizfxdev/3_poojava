# Exercício Resolvido: Sistema de Etiquetagem de Produtos — Versão Detalhada

## 1. Visão Geral do Problema

### 1.1 O Contexto Real

Imagine uma loja online que vende produtos de diferentes origens e condições:

- **Produtos Comuns:** Produtos novos comprados localmente. 
  - Etiqueta: Nome + Preço final
  - Exemplo: "Caneta - $ 5.00"

- **Produtos Importados:** Trazem taxa de alfândega (imposto de importação).
  - Etiqueta: Nome + (Preço + Taxa alfândega) + valor da taxa destacado
  - Exemplo: "Notebook - $ 3600.00 (Customs fee: $ 600.00)"

- **Produtos Usados:** Itens de segunda mão com data de fabricação.
  - Etiqueta: Nome + Preço + data de fabricação
  - Exemplo: "Geladeira - $ 1500.00 (Manufacture date: 15/03/2017)"

### 1.2 O Desafio

**Como gerar etiquetas DIFERENTES para cada tipo SEM repetir código E SEM usar dezenas de `if/else`?**

#### ❌ Abordagem Ruim — Sem Herança

```java
// Classe 1
public class CommonProduct {
    public String name;    // REPETIÇÃO!
    public double price;   // REPETIÇÃO!
    
    public String priceTag() {
        return name + " - $ " + price;
    }
}

// Classe 2
public class ImportedProduct {
    public String name;          // REPETIÇÃO!
    public double price;         // REPETIÇÃO!
    public double customsFee;
    
    public String priceTag() {
        double finalPrice = price + customsFee;
        return name + " - $ " + finalPrice + " (Customs fee: $ " + customsFee + ")";
    }
}

// Classe 3
public class UsedProduct {
    public String name;             // REPETIÇÃO!
    public double price;            // REPETIÇÃO!
    public LocalDate manuDate;
    
    public String priceTag() {
        return name + " - $ " + price + " (Manufacture date: " + manuDate + ")";
    }
}

// Para usar:
List<Object> products = new ArrayList<>();  // Object? Péssimo!
products.add(new CommonProduct());
products.add(new ImportedProduct());
products.add(new UsedProduct());

for (Object p : products) {
    if (p instanceof CommonProduct) {
        System.out.println(((CommonProduct)p).priceTag());
    } else if (p instanceof ImportedProduct) {
        System.out.println(((ImportedProduct)p).priceTag());
    } else if (p instanceof UsedProduct) {
        System.out.println(((UsedProduct)p).priceTag());
    }
}
```

**Problemas:**
- ❌ Código `name` e `price` repetido 3 vezes
- ❌ Múltiplos `if/else instanceof`
- ❌ Se adicionar novo tipo: copiar tudo novamente
- ❌ Se mudar formato compartilhado: alterar 3 classes
- ❌ Usar `Object` = perder type-safety

#### ✅ Abordagem Boa — Com Herança + Polimorfismo

```java
// Superclasse = código comum em UM lugar
public abstract class Product {
    protected String name;        // Uma única vez!
    protected double price;       // Uma única vez!
    
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public abstract String priceTag();  // Cada um faz do seu jeito
}

// Subclasses = especializam apenas o que é diferente
public class CommonProduct extends Product {
    @Override
    public String priceTag() {
        return name + " - $ " + price;
    }
}

public class ImportedProduct extends Product {
    private double customsFee;
    
    @Override
    public String priceTag() {
        double finalPrice = price + customsFee;
        return name + " - $ " + finalPrice + " (Customs fee: $ " + customsFee + ")";
    }
}

public class UsedProduct extends Product {
    private LocalDate manufactureDate;
    
    @Override
    public String priceTag() {
        return name + " - $ " + price + " (Manufacture date: " + manufactureDate + ")";
    }
}

// Para usar:
List<Product> products = new ArrayList<>();  // Type-safe!
products.add(new CommonProduct(...));
products.add(new ImportedProduct(...));
products.add(new UsedProduct(...));

for (Product p : products) {
    System.out.println(p.priceTag());  // Um único line, sem if/else!
}
```

**Vantagens:**
- ✅ `name` e `price` definidos UM única vez
- ✅ Sem `if/else` — polimorfismo decide
- ✅ Adicionar novo tipo: criar nova classe, pronto!
- ✅ Modificar compartilhado: alterar 1 classe
- ✅ `List<Product>` = type-safe

### 1.3 Requisitos Funcionais

1. **Entrada:** Ler N produtos (N informado pelo usuário)
2. **Heterogeneidade:** Cada produto pode ser de qualquer tipo (c/i/u)
3. **Armazenamento:** Guardar todos em UMA lista única
4. **Processamento:** Exibir etiqueta de preço de cada um
5. **Formato:** Entrada/saída exatas conforme especificado

### 1.4 Exemplo de Execução Completa

```
Enter the number of products: 3

Product #1 data:
Common, imported or used (c/i/u)? i
Name: Notebook
Price: 3000.00
Customs fee: 600.00

Product #2 data:
Common, imported or used (c/i/u)? u
Name: Geladeira
Price: 1500.00
Manufacture date (dd/MM/yyyy): 15/03/2017

Product #3 data:
Common, imported or used (c/i/u)? c
Name: Caneta
Price: 5.00

========== PRICE TAGS ==========
Notebook - $ 3600.00 (Customs fee: $ 600.00)
Geladeira - $ 1500.00 (Manufacture date: 15/03/2017)
Caneta - $ 5.00
```

---

## 2. Estrutura de Classes — Análise Profunda

### 2.1 Hierarquia de Classes

```
                 Product (Abstrata)
                    ↑ ↑ ↑
                    | | |
                    | | └─────────────────────┐
                    | └────────────────┐      |
                    └─────────┐        |      |
                              |        |      |
                        CommonProduct  |   UsedProduct
                                       |
                                 ImportedProduct
```

### 2.2 Responsabilidades e Decisões

| Aspecto | Classe | Razão |
|--------|--------|-------|
| **Dados Comuns** | `Product` | `name`, `price` usados por TODAS as subclasses |
| **Contrato** | `Product` | Define que toda subclasse DEVE implementar `priceTag()` |
| **Cálculo Simples** | `CommonProduct` | Etiqueta básica: nome + preço |
| **Cálculo Complexo** | `ImportedProduct` | Etiqueta + taxa alfândega (adição) |
| **Cálculo Complementar** | `UsedProduct` | Etiqueta + informação de data (sem alterar preço) |
| **Orquestração** | `Program` | Lê, armazena, exibe usando polimorfismo |

### 2.3 Por Que `Product` É Abstrata?

**Pergunta:** Por que não deixar Product concreta?

**Resposta:** Porque não existe um "produto genérico" válido no sistema.

```java
// CENÁRIO 1: Um código tenta criar um produto genérico
Product p = new Product("Algo", 100.0);

// Problema: Como gerar a etiqueta?
// - Se fosse CommonProduct: faltaria informação de tax/date
// - Se fosse ImportedProduct: teríamos taxa = 0?
// - Se fosse UsedProduct: teríamos data = null?
// Nenhuma opção faz sentido!

// CENÁRIO 2: Code cliente não sabe qual tipo é
Product p = list.get(0);  // Pode ser qualquer um!

// Solução: Deixar product.priceTag() ser chamado
// Java automaticamente sabe qual versão chamar
// Sem precisar de if/else instanceof
```

**Decisão:** Deixar `Product` abstrata **OBRIGA** que seja sempre instanciado como uma subclasse concreta. Nenhuma ambiguidade.

### 2.4 Por Que `priceTag()` É Abstrato?

**Pergunta:** Por que não implementar `priceTag()` na superclasse?

**Resposta:** Porque a lógica é COMPLETAMENTE diferente para cada tipo.

```
CommonProduct.priceTag():
┌──────────────────────────┐
│ name + " - $ " + price   │
│ "Caneta - $ 5.00"        │
└──────────────────────────┘

ImportedProduct.priceTag():
┌──────────────────────────────────────────────────────┐
│ name + " - $ " + (price + customsFee) + " (Customs   │
│ fee: $ " + customsFee + ")"                          │
│ "Notebook - $ 3600.00 (Customs fee: $ 600.00)"      │
└──────────────────────────────────────────────────────┘

UsedProduct.priceTag():
┌──────────────────────────────────────────────────────┐
│ name + " - $ " + price + " (Manufacture date: " +   │
│ manufactureDate.format(...) + ")"                    │
│ "Geladeira - $ 1500.00 (Manufacture date:           │
│ 15/03/2017)"                                         │
└──────────────────────────────────────────────────────┘
```

Todas as implementações são diferentes. Não há código comum. Deixar abstrato força cada uma a implementar corretamente.

---

## 3. Código Fonte — Linha por Linha

### 3.1 Arquivo: `Product.java` — A Superclasse Abstrata

```java
// === DECLARAÇÃO ===
// "abstract" = não pode fazer new Product()
// Força instanciação apenas via subclasses concretas
public abstract class Product {
    
    // === ATRIBUTOS ===
    // "protected" = subclasses PODEM acessar (melhor que private)
    // Se fosse "private": subclasses não conseguiriam usar
    // Se fosse "public": qualquer um poderia modificar (péssimo)
    // protected = encapsulamento balanceado
    protected String name;
    protected double price;
    
    // === CONSTRUTOR ===
    // Inicializa atributos comuns A TODAS as subclasses
    // Cada subclasse chama super(...) para reutilizar este construtor
    // Evita duplicação de lógica de inicialização
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    // === ACESSORES (Getters) ===
    // Permitem que Program e subclasses LEIAM os dados
    // Não existem setters = dados imutáveis após criação
    // (Produtos não devem mudar de nome/preço depois de criados)
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    // === MÉTODO ABSTRATO ===
    // Sem corpo de implementação (sem {  })
    // "abstract" = OBRIGA subclasses a implementar
    // Compilador verifica: se subclasse não implementar → ERROR
    // 
    // Cada subclasse produz formato diferente:
    //   - CommonProduct.priceTag()   → "Caneta - $ 5.00"
    //   - ImportedProduct.priceTag() → "Notebook - $ 3600.00 (Customs fee: $ 600.00)"
    //   - UsedProduct.priceTag()     → "Geladeira - $ 1500.00 (Manufacture date: 15/03/2017)"
    //
    // Quando Program chama product.priceTag(), Java descobre qual versão usar
    // Sem nenhum if/else!
    public abstract String priceTag();
}
```

**Pontos Críticos:**
- `abstract class` = contrato, não implementação
- `protected` = balanço entre encapsulamento e reutilização
- `abstract method` = força especialização

---

### 3.2 Arquivo: `CommonProduct.java` — Etiqueta Simples

```java
// === HERANÇA ===
// "extends Product" = CommonProduct herda name, price, getName(), getPrice()
// CommonProduct adiciona: nada além de uma implementação de priceTag()
public class CommonProduct extends Product {
    
    // === CONSTRUTOR ===
    // Chama super(...) para inicializar os atributos da superclasse
    // Depois, nada mais a inicializar (CommonProduct não tem atributos próprios)
    public CommonProduct(String name, double price) {
        super(name, price);  // Passa para Product.__init__
    }
    
    // === IMPLEMENTAÇÃO DO MÉTODO ABSTRATO ===
    // @Override = anotação que informa ao compilador que estamos sobrescrevendo
    // Ajuda detectar typos (ex: "priceTag_" em vez de "priceTag")
    //
    // Lógica: etiqueta simples com nome e preço
    // Nenhuma adição, nenhuma subtração
    @Override
    public String priceTag() {
        // String.format = modo seguro de formatar números com decimais
        // %.2f = mostrar sempre com 2 casas decimais
        // Exemplo: 5.00 (não 5 nem 5.0)
        return String.format("%s - $ %.2f", name, price);
    }
}
```

**Observação:** CommonProduct não precisa de atributos extras. Usa name/price da superclasse e implementa apenas `priceTag()`.

---

### 3.3 Arquivo: `ImportedProduct.java` — Com Taxa de Alfândega

```java
// === ESPECIALIZAÇÃO ===
// ImportedProduct HERDA tudo de Product E ADICIONA taxa alfândega
public class ImportedProduct extends Product {
    
    // === ATRIBUTO ESPECÍFICO ===
    // Dado que APENAS este tipo possui
    // private = ImportedProduct controla este valor
    private double customsFee;
    
    // === CONSTRUTOR ===
    // Recebe 3 parâmetros: nome, preço (da superclasse) e taxa
    // super(name, price) = inicializa superclasse
    // this.customsFee = inicializa atributo próprio
    public ImportedProduct(String name, double price, double customsFee) {
        super(name, price);          // Inicializa name e price
        this.customsFee = customsFee;  // Inicializa taxa
    }
    
    // === ACESSOR ESPECÍFICO ===
    // Permite que Program leia a taxa
    public double getCustomsFee() {
        return customsFee;
    }
    
    // === IMPLEMENTAÇÃO ESPECIALIZADA ===
    // Calcula preço final = preço + taxa
    // Exibe ambos na etiqueta
    @Override
    public String priceTag() {
        // Primeira linha: calcula preço final
        double finalPrice = price + customsFee;
        
        // Segunda linha: formata a etiqueta com informação de taxa
        // String.format permite múltiplas substituições:
        //   %s = name (string)
        //   %.2f = finalPrice (double com 2 casas)
        //   %.2f = customsFee (double com 2 casas)
        return String.format("%s - $ %.2f (Customs fee: $ %.2f)", 
                           name, finalPrice, customsFee);
        
        // Exemplo:
        // name = "Notebook", price = 3000.00, customsFee = 600.00
        // Resultado: "Notebook - $ 3600.00 (Customs fee: $ 600.00)"
    }
}
```

**Fluxo de Dados:**
```
usuário digita:
  Name: Notebook
  Price: 3000.00
  Customs fee: 600.00
         ↓
Construtor ImportedProduct(...)
  - super("Notebook", 3000.00) → Product inicializa name, price
  - this.customsFee = 600.00
         ↓
Chamada de .priceTag()
  - finalPrice = 3000.00 + 600.00 = 3600.00
  - return "Notebook - $ 3600.00 (Customs fee: $ 600.00)"
```

---

### 3.4 Arquivo: `UsedProduct.java` — Com Data de Fabricação

```java
// === ESPECIALIZAÇÃO ===
// UsedProduct HERDA de Product E ADICIONA data de fabricação
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UsedProduct extends Product {
    
    // === ATRIBUTO ESPECÍFICO ===
    // LocalDate = classe padrão Java para datas (sem hora)
    // private = apenas UsedProduct controla este valor
    private LocalDate manufactureDate;
    
    // === CONSTRUTOR ===
    // Recebe: name, price (superclasse) e date (próprio)
    public UsedProduct(String name, double price, LocalDate manufactureDate) {
        super(name, price);              // Inicializa superclasse
        this.manufactureDate = manufactureDate;  // Inicializa data
    }
    
    // === ACESSOR ESPECÍFICO ===
    // Permite que Program leia a data
    public LocalDate getManufactureDate() {
        return manufactureDate;
    }
    
    // === IMPLEMENTAÇÃO ESPECIALIZADA ===
    // Inclui a data de fabricação na etiqueta
    // IMPORTANTE: NÃO adiciona à etiqueta do preço
    // (o preço já reflete que é um produto usado)
    @Override
    public String priceTag() {
        // Formata a data de LOCALDATE para STRING "dd/MM/yyyy"
        // Exemplo: LocalDate.of(2017, 3, 15) → "15/03/2017"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = manufactureDate.format(formatter);
        
        // Retorna etiqueta com nome, preço e data
        return String.format("%s - $ %.2f (Manufacture date: %s)", 
                           name, price, formattedDate);
        
        // Exemplo:
        // name = "Geladeira", price = 1500.00, date = 15/03/2017
        // Resultado: "Geladeira - $ 1500.00 (Manufacture date: 15/03/2017)"
    }
}
```

**Nota Importante:** O preço NÃO é aumentado ou diminuído. A data é apenas informação adicional. O preço já reflete a condição usada.

---

### 3.5 Arquivo: `Program.java` — A Orquestração

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) {
        // === CONFIGURAÇÃO LOCALE ===
        // Locale.US = usar ponto como separador decimal (3000.00, não 3000,00)
        Locale.setDefault(Locale.US);
        
        // === ENTRADA ===
        // Scanner = ler dados do teclado
        Scanner sc = new Scanner(System.in);
        
        // === ARMAZENAMENTO ===
        // List<Product> = uma lista que contém QUALQUER subclasse de Product
        // Isso é polimorfismo! Uma variável pode apontar para diferentes tipos
        List<Product> products = new ArrayList<>();
        
        // === LEITURA DE DADOS ===
        System.out.print("Enter the number of products: ");
        int n = sc.nextInt();
        sc.nextLine();  // Limpar buffer (nextInt não consome a quebra de linha)
        
        // Loop para ler N produtos
        for (int i = 1; i <= n; i++) {
            System.out.println("\nProduct #" + i + " data:");
            
            // Ler tipo de produto
            System.out.print("Common, imported or used (c/i/u)? ");
            char type = sc.nextLine().charAt(0);  // Pega primeiro caractere
            
            // Ler dados comuns (nome e preço)
            System.out.print("Name: ");
            String name = sc.nextLine();
            
            System.out.print("Price: ");
            double price = sc.nextDouble();
            sc.nextLine();  // Limpar buffer
            
            // === CRIAÇÃO POLIMÓRFICA ===
            // Baseado no tipo (c/i/u), cria a subclasse apropriada
            // Mas TODOS são armazenados como "Product"
            if (type == 'i') {
                // === PRODUTO IMPORTADO ===
                System.out.print("Customs fee: ");
                double customsFee = sc.nextDouble();
                sc.nextLine();
                
                // Criar e adicionar à lista
                // Note: está sendo adicionado como "Product" (superclasse)
                // Mas internamente é um ImportedProduct
                products.add(new ImportedProduct(name, price, customsFee));
                
            } else if (type == 'u') {
                // === PRODUTO USADO ===
                System.out.print("Manufacture date (dd/MM/yyyy): ");
                String dateStr = sc.nextLine();
                
                // Parser: converter string "15/03/2017" em LocalDate
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(dateStr, dtf);
                
                // Criar e adicionar à lista
                products.add(new UsedProduct(name, price, date));
                
            } else {
                // === PRODUTO COMUM ===
                // type == 'c'
                // Criar e adicionar à lista
                products.add(new CommonProduct(name, price));
            }
        }
        
        // === EXIBIÇÃO DE ETIQUETAS ===
        System.out.println("\n========== PRICE TAGS ==========");
        
        // Loop sobre a lista
        // POLIMORFISMO EM AÇÃO!
        for (Product p : products) {
            // Cada p é um tipo diferente (CommonProduct, ImportedProduct, UsedProduct)
            // Mas todos são referenciados como "Product"
            // Quando chama p.priceTag(), Java sabe qual versão chamar:
            //   - Se p é CommonProduct, chama CommonProduct.priceTag()
            //   - Se p é ImportedProduct, chama ImportedProduct.priceTag()
            //   - Se p é UsedProduct, chama UsedProduct.priceTag()
            // Sem nenhum if/else!
            System.out.println(p.priceTag());
        }
        
        // === LIMPEZA ===
        sc.close();
    }
}
```

**Fluxo Completo:**

```
1. Ler quantidade N
   ↓
2. Para cada i de 1 a N:
   a. Ler tipo (c/i/u)
   b. Ler nome e preço
   c. Se tipo == 'i':
        Ler taxa
        Criar ImportedProduct
      Se tipo == 'u':
        Ler data
        Criar UsedProduct
      Se tipo == 'c':
        Criar CommonProduct
   d. Adicionar à List<Product>
   ↓
3. Para cada produto na lista:
   a. Chamar product.priceTag()
      (Java descobre qual versão automaticamente)
   b. Exibir resultado
```

---

## 4. O Polimorfismo em Ação

### 4.1 Simulação Passo-a-Passo

**Entrada do Usuário:**
```
Enter the number of products: 2

Product #1 data:
Common, imported or used (c/i/u)? i
Name: Notebook
Price: 3000.00
Customs fee: 600.00

Product #2 data:
Common, imported or used (c/i/u)? c
Name: Pen
Price: 5.00
```

**Execução Interna:**

```
1. n = 2
2. Iteração i=1:
   type='i', name="Notebook", price=3000.00, customsFee=600.00
   → new ImportedProduct("Notebook", 3000.00, 600.00)
   → products.add(...)
   → products[0] = ImportedProduct instance
   
3. Iteração i=2:
   type='c', name="Pen", price=5.00
   → new CommonProduct("Pen", 5.00)
   → products.add(...)
   → products[1] = CommonProduct instance
   
4. Exibição:
   for (Product p : products) {  // p recebe cada elemento
       System.out.println(p.priceTag());
   }
   
   Iteração 1:
   p aponta para ImportedProduct
   p.priceTag() → chama ImportedProduct.priceTag()
   → "Notebook - $ 3600.00 (Customs fee: $ 600.00)"
   
   Iteração 2:
   p aponta para CommonProduct
   p.priceTag() → chama CommonProduct.priceTag()
   → "Pen - $ 5.00"
   
Output:
========== PRICE TAGS ==========
Notebook - $ 3600.00 (Customs fee: $ 600.00)
Pen - $ 5.00
```

### 4.2 Por Que Isso É Polimorfismo?

**Definição:** Uma mesma chamada (`p.priceTag()`) produz DIFERENTES resultados dependendo do TIPO REAL do objeto.

```java
Product p = ...;  // Pode ser qualquer uma das 3 subclasses
p.priceTag();     // Mesma chamada
              // Mas resultado diferente:
              //   CommonProduct → "Nome - $ Preço"
              //   ImportedProduct → "Nome - $ (Preço+Taxa) (Taxa: X)"
              //   UsedProduct → "Nome - $ Preço (Data: D)"
```

**Sem Polimorfismo (seria assim):**
```java
if (p instanceof CommonProduct) {
    System.out.println(((CommonProduct)p).priceTag());
} else if (p instanceof ImportedProduct) {
    System.out.println(((ImportedProduct)p).priceTag());
} else if (p instanceof UsedProduct) {
    System.out.println(((UsedProduct)p).priceTag());
}
```

**Com Polimorfismo (como é agora):**
```java
System.out.println(p.priceTag());  // UMA linha!
```

---

## 5. Vantagens Arquiteturais

### 5.1 Reuso de Código

**Sem herança:**
```java
// name e price REPETIDOS em cada classe
class CommonProduct { String name; double price; }
class ImportedProduct { String name; double price; double customsFee; }
class UsedProduct { String name; double price; LocalDate date; }

// Se mudar formato de name ou price: 3 classes alteradas
```

**Com herança:**
```java
class Product { String name; double price; }  // UMA definição
class CommonProduct extends Product { }        // Herda automaticamente
class ImportedProduct extends Product { }      // Herda automaticamente
class UsedProduct extends Product { }          // Herda automaticamente

// Se mudar: altera 1 classe
```

### 5.2 Extensibilidade

**Para adicionar novo tipo (ex: DiscountedProduct):**

```java
public class DiscountedProduct extends Product {
    private double discountPercentage;
    
    public DiscountedProduct(String name, double price, double discount) {
        super(name, price);
        this.discountPercentage = discount;
    }
    
    @Override
    public String priceTag() {
        double finalPrice = price * (1 - discountPercentage / 100);
        return String.format("%s - $ %.2f (Discount: %.1f%%)", 
                           name, finalPrice, discountPercentage);
    }
}

// Program.java NÃO precisa mudar!
// Loop existente automaticamente funciona com novo tipo
```

### 5.3 Manutenibilidade

**Cenário: Mudar formato de etiqueta de todos os tipos**

Sem polimorfismo:
- Alterar CommonProduct.priceTag()
- Alterar ImportedProduct.priceTag()
- Alterar UsedProduct.priceTag()
- **3 mudanças, 3 pontos de falha**

Com polimorfismo:
- Cada classe é independente
- Mudar CommonProduct não afeta ImportedProduct
- **Mais fácil debugar, menos risco de regressão**

---

## 6. Teste e Validação

### 6.1 Casos de Teste

**Teste 1: Produto Importado**
```
Input: type=i, name="Laptop", price=2500, fee=500
Expected: "Laptop - $ 3000.00 (Customs fee: $ 500.00)"
```

**Teste 2: Produto Usado**
```
Input: type=u, name="Microwave", price=300, date=10/05/2019
Expected: "Microwave - $ 300.00 (Manufacture date: 10/05/2019)"
```

**Teste 3: Produto Comum**
```
Input: type=c, name="Pencil", price=2
Expected: "Pencil - $ 2.00"
```

---

## 7. Conceitos-Chave Aplicados

| Conceito | Aplicação |
|----------|-----------|
| **Herança** | Product base, 3 subclasses reutilizam name/price |
| **Classe Abstrata** | Product = contrato, não implementação |
| **Método Abstrato** | priceTag() = cada subclasse faz do seu jeito |
| **@Override** | Marca que subclasse sobrescreve superclasse |
| **protected** | name/price acessíveis por subclasses, protegidas de fora |
| **Polimorfismo** | List<Product> contém tipos diferentes, um loop executa todos |
| **Encapsulamento** | customsFee/manufactureDate privados, acessados via getter |

---

## 8. Checklist de Aprendizado

- [ ] Entendo por que Product é abstrata
- [ ] Entendo por que priceTag() é abstrato
- [ ] Entendo o fluxo de herança com super()
- [ ] Entendo a criação polimórfica (new ImportedProduct mas atribuir a Product)
- [ ] Entendo por que o loop é UMA linha, sem if/else
- [ ] Entendo como adicionar novo tipo sem alterar Program.java
- [ ] Consigo traçar a execução passo-a-passo
- [ ] Consigo modificar o formato de etiqueta em uma subclasse sem quebrar outras

---

## 9. Conclusão

Este exercício demonstra que **herança + polimorfismo** não é apenas teoria — é a solução elegante para um problema prático real. O mesmo padrão pode ser aplicado em: banco (conta comum/poupança/empresa), animais (mamífero/ave/peixe), funcionários (comum/gerente/terceirizado), e muito mais.

**Bom estudo!** 🚀