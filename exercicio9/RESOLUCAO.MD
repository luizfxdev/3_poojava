# Resolução Comentada - Exercício de Figuras Geométricas

## Explicação Linha por Linha

### 1. **Enum Color**
```java
public enum Color {
    BLACK(0), BLUE(1), RED(2);
    
    private int value;
    Color(int value) { this.value = value; }
}
```
- **Enum**: Define constantes (BLACK, BLUE, RED) com valores inteiros.
- Cada cor tem um `value` associado (pode ser usado para persistência ou comparações).
- Usamos `Color.valueOf(colorStr)` para converter string em enum.

---

### 2. **Classe Abstrata Shape**
```java
public abstract class Shape {
    protected Color color;
    
    public Shape(Color color) {
        this.color = color;
    }
    
    public abstract double area();
}
```
- **`abstract`**: Não pode ser instanciada diretamente.
- **Atributo `color`**: Compartilhado por todas as figuras (herança).
- **Método `area()`**: Abstrato — cada subclasse implementa seu próprio cálculo.
- O construtor recebe a cor e atribui ao atributo protegido.

---

### 3. **Classe Rectangle**
```java
public class Rectangle extends Shape {
    private double width;
    private double height;
    
    public Rectangle(Color color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
}
```
- **`extends Shape`**: Herda de Shape; obrigada a implementar `area()`.
- **`super(color)`**: Chama o construtor da classe pai.
- **`@Override`**: Marca que está implementando método abstrato (não obrigatório, mas boa prática).
- Área do retângulo: `width × height`.

---

### 4. **Classe Circle**
```java
public class Circle extends Shape {
    private double radius;
    
    public Circle(Color color, double radius) {
        super(color);
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}
```
- Mesma estrutura que Rectangle.
- Área do círculo: `π × r²`.

---

### 5. **Classe Main - Lógica Principal**
```java
List<Shape> shapes = new ArrayList<>();
```
- **List<Shape>**: Lista polimórfica que armazena Rectangle E Circle (ambas são Shape).

```java
for (int i = 0; i < n; i++) {
    // ... lê tipo, cor, dimensões ...
    
    if (type == 'r') {
        shape = new Rectangle(color, width, height);
    } else {
        shape = new Circle(color, radius);
    }
    
    shapes.add(shape);
}
```
- **Polimorfismo**: Independente se é Rectangle ou Circle, ambos são armazenados como Shape.

```java
for (Shape shape : shapes) {
    System.out.println(String.format("%.2f", shape.area()));
}
```
- **Chamada polimórfica**: Cada Shape chama seu próprio `area()` (Rectangle calcula width×height, Circle calcula π×r²).
- `String.format("%.2f", ...)`: Formata com 2 casas decimais.

---

## Resumo: Classes e Métodos Abstratos

### O que é uma Classe Abstrata?
Uma classe que **não pode ser instanciada** (não dá `new Shape()`), mas pode ser **herdada**. Serve como modelo/contrato para subclasses.

```java
// ❌ NÃO FUNCIONA
Shape s = new Shape(Color.BLACK);

// ✅ FUNCIONA
Shape s = new Rectangle(Color.BLACK, 4.0, 5.0);
```

### O que é um Método Abstrato?
Método sem implementação (`{ }` vazio). Cada subclasse **obrigatoriamente** implementa o seu.

```java
// Na classe abstrata
public abstract double area();

// Na subclasse (obrigatório)
@Override
public double area() {
    return width * height;
}
```

### Por que Usar?

| Conceito | Benefício |
|----------|-----------|
| **Abstração** | Força implementação correta em subclasses |
| **Polimorfismo** | Mesma chamada (`shape.area()`) funciona para todos |
| **Reutilização** | Atributo `color` fica em Shape, não se repete |
| **Contrato** | Toda figura geométrica deve ter uma área |

### Fluxo Prático
1. Define `Shape` com método `area()` abstrato.
2. Rectangle e Circle herdam e implementam `area()` cada uma à sua forma.
3. Main cria uma lista de Shape, mas adiciona Rectangle/Circle nela.
4. Ao chamar `shape.area()`, Java sabe qual implementação usar (polimorfismo).

**Vantagem**: Se no futuro você quiser adicionar Triangle, só herda Shape e implementa `area()` — nenhuma mudança em Main.