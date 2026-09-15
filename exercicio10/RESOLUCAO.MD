# Reserva de Veículos com Tratamento de Exceções

## Sobre o Desafio

Fazer um programa para ler os dados de uma reserva de veículo (placa, data de saída e data de devolução) e mostrar os dados da reserva, incluindo sua duração em dias. Em seguida, ler novas datas de saída e devolução, atualizar a reserva, e mostrar novamente a reserva com os dados atualizados. O programa não deve aceitar dados inválidos para a reserva, conforme as seguintes regras:

- Alterações de reserva só podem ocorrer para datas futuras
- A data de devolução deve ser maior que a data de saída

---

## Resolução Linha por Linha

### 1. Classe PastDateException

```java
public class PastDateException extends Exception {
    public PastDateException(String message) {
        super(message);
    }
}
```

- **`extends Exception`**: Herda de Exception (exceção checked). Obrigatório capturar com try-catch ou declarar com throws.
- **Construtor**: Recebe uma mensagem de erro e passa para classe pai via `super(message)`.
- Lançada quando checkout ou returnDate estão no passado.

---

### 2. Classe InvalidDateRangeException

```java
public class InvalidDateRangeException extends Exception {
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
```

- **`extends Exception`**: Herda de Exception (exceção checked).
- Lançada quando returnDate ≤ checkoutDate.

---

### 3. Classe VehicleReservation - Atributos e Construtor

```java
public class VehicleReservation {
    private String licensePlate;
    private LocalDate checkoutDate;
    private LocalDate returnDate;
    
    public VehicleReservation(String licensePlate, LocalDate checkoutDate, LocalDate returnDate) 
            throws PastDateException, InvalidDateRangeException {
        validate(checkoutDate, returnDate);
        this.licensePlate = licensePlate;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
    }
}
```

- **Atributos privados**: Encapsulamento de dados.
- **`throws`**: Declara que o construtor pode lançar essas exceções. Obriga quem chamar a tratar.
- **`validate(...)`**: Chama validação antes de atribuir valores.

---

### 4. Classe VehicleReservation - Método validate()

```java
private void validate(LocalDate checkoutDate, LocalDate returnDate) 
        throws PastDateException, InvalidDateRangeException {
    LocalDate today = LocalDate.now();
    
    if (checkoutDate.isBefore(today)) {
        throw new PastDateException("A data de saída não pode ser no passado");
    }
    
    if (returnDate.isBefore(today)) {
        throw new PastDateException("A data de devolução não pode ser no passado");
    }
    
    if (!returnDate.isAfter(checkoutDate)) {
        throw new InvalidDateRangeException("A data de devolução deve ser após a data de saída");
    }
}
```

- **`LocalDate.now()`**: Obtém data de hoje.
- **`checkoutDate.isBefore(today)`**: Se checkout está no passado, lança PastDateException.
- **`returnDate.isBefore(today)`**: Se return está no passado, lança PastDateException.
- **`!returnDate.isAfter(checkoutDate)`**: Se return não é depois que checkout, lança InvalidDateRangeException.
- **`throw new Exception(...)`**: Cria e lança a exceção com mensagem de erro.

---

### 5. Classe VehicleReservation - Método updateDates()

```java
public void updateDates(LocalDate newCheckoutDate, LocalDate newReturnDate) 
        throws PastDateException, InvalidDateRangeException {
    validate(newCheckoutDate, newReturnDate);
    this.checkoutDate = newCheckoutDate;
    this.returnDate = newReturnDate;
}
```

- Reutiliza `validate()` para mesmas regras de negócio.
- Só atualiza se validação passar (sem exceção).

---

### 6. Classe VehicleReservation - Métodos auxiliares

```java
public long getDurationDays() {
    return ChronoUnit.DAYS.between(checkoutDate, returnDate);
}

@Override
public String toString() {
    return String.format(
        "Placa: %s%nData de saída: %s%nData de devolução: %s%nDuração: %d dias",
        licensePlate, checkoutDate, returnDate, getDurationDays()
    );
}
```

- **`ChronoUnit.DAYS.between(...)`**: Calcula quantidade de dias entre duas datas.
- **`String.format(...)`**: Formata string com valores interpolados. `%n` = nova linha.
- **`@Override`**: Sobrescreve método toString() de Object.

---

### 7. Classe Main - Leitura de entrada

```java
Scanner sc = new Scanner(System.in);
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

try {
    System.out.print("License plate: ");
    String licensePlate = sc.nextLine();
    
    System.out.print("Checkout date (dd/MM/yyyy): ");
    LocalDate checkoutDate = LocalDate.parse(sc.nextLine(), fmt);
    
    System.out.print("Return date (dd/MM/yyyy): ");
    LocalDate returnDate = LocalDate.parse(sc.nextLine(), fmt);
    
    VehicleReservation reservation = new VehicleReservation(licensePlate, checkoutDate, returnDate);
```

- **`DateTimeFormatter.ofPattern("dd/MM/yyyy")`**: Define formato de data esperado.
- **`LocalDate.parse(string, formatter)`**: Converte string em LocalDate. Lança exceção se formato inválido.
- **`try`**: Início do bloco de tratamento de exceções.
- Se VehicleReservation lançar exceção, pula para catch correspondente.

---

### 8. Classe Main - Exibição e atualização

```java
    System.out.println("\nReservation data:");
    System.out.println(reservation);
    
    System.out.print("\nNew checkout date (dd/MM/yyyy): ");
    LocalDate newCheckoutDate = LocalDate.parse(sc.nextLine(), fmt);
    
    System.out.print("New return date (dd/MM/yyyy): ");
    LocalDate newReturnDate = LocalDate.parse(sc.nextLine(), fmt);
    
    reservation.updateDates(newCheckoutDate, newReturnDate);
    
    System.out.println("\nReservation data (updated):");
    System.out.println(reservation);
```

- **`System.out.println(reservation)`**: Chama `toString()` automaticamente.
- **`updateDates(...)`**: Se lançar exceção, pula para catch.
- Só exibe dados atualizados se atualização for bem-sucedida.

---

### 9. Classe Main - Tratamento de exceções

```java
} catch (PastDateException e) {
    System.out.println("ERROR: " + e.getMessage());
} catch (InvalidDateRangeException e) {
    System.out.println("ERROR: " + e.getMessage());
} catch (Exception e) {
    System.out.println("ERROR: " + e.getMessage());
} finally {
    sc.close();
}
```

- **`catch (PastDateException e)`**: Captura especificamente PastDateException.
- **`catch (InvalidDateRangeException e)`**: Captura especificamente InvalidDateRangeException.
- **`catch (Exception e)`**: Captura qualquer outra exceção não prevista (ex: formato de data inválido).
- **`finally`**: **Sempre executa**, mesmo com exceção. Fecha Scanner.
- **Ordem importa**: Mais específico antes, genérico por último.

---

## Fluxo Esperado - Sucesso

```
Placa do veículo: ABC-1234
Data de saída (dd/MM/yyyy): 20/12/2025
Data de devolução (dd/MM/yyyy): 25/12/2025

Dados da reserva:
Placa: ABC-1234
Data de saída: 2025-12-20
Data de devolução: 2025-12-25
Duração: 5 dias

Nova data de saída (dd/MM/yyyy): 22/12/2025
Nova data de devolução (dd/MM/yyyy): 27/12/2025

Dados da reserva (atualizado):
Placa: ABC-1234
Data de saída: 2025-12-22
Data de devolução: 2025-12-27
Duração: 5 dias
```

---

## Fluxo Esperado - Erro

```
Placa do veículo: ABC-1234
Data de saída (dd/MM/yyyy): 15/09/2024
Data de devolução (dd/MM/yyyy): 20/09/2024

ERRO: A data de saída não pode ser no passado
```

Scanner fecha no `finally`, programa encerra.