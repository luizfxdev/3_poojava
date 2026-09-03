# Exercício: Conversor de Eventos com Timezone

## Enunciado

Uma API recebe um evento agendado em UTC: `"2024-08-15T20:00:00Z"` (livestream)

Você precisa:
1. Converter para horário de São Paulo (UTC-3)
2. Calcular quantas horas faltam até o evento
3. Formatar para exibição: `"15/08/2024 às 17h (São Paulo)"`

---

## Solução Completa

```java
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ConversorEvento {
    public static void main(String[] args) {
        
        // 1?? Parse da string ISO 8601 (UTC)
        String eventoUTC = "2024-08-15T20:00:00Z";
        Instant instant = Instant.parse(eventoUTC);
        
        // 2?? Definir timezone de São Paulo
        ZoneId zonaSpSP = ZoneId.of("America/Sao_Paulo");
        
        // 3?? Converter Instant (UTC) ? LocalDateTime (São Paulo)
        LocalDateTime eventoLocal = LocalDateTime.ofInstant(instant, zonaSpSP);
        
        // 4?? Calcular diferença até agora
        LocalDateTime agora = LocalDateTime.now(zonaSpSP);
        long horasRestantes = ChronoUnit.HOURS.between(agora, eventoLocal);
        
        // 5?? Formatar para exibição
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH");
        
        String dataFormatada = eventoLocal.format(fmt);
        String horaFormatada = eventoLocal.format(fmtHora);
        
        // 6?? Montar string final
        String resultado = String.format(
            "%s às %sh (São Paulo)",
            dataFormatada,
            horaFormatada
        );
        
        // 7?? Exibir resultado
        System.out.println("Evento: " + resultado);
        System.out.println("Faltam " + horasRestantes + " horas");
    }
}
```

---

## Explicação Linha por Linha

### **Bloco 1: Parse da String ISO 8601**
```java
String eventoUTC = "2024-08-15T20:00:00Z";
Instant instant = Instant.parse(eventoUTC);
```
- Recebe string no formato ISO 8601 (com `Z` indicando UTC)
- `Instant.parse()` converte para um objeto `Instant` (sempre UTC internamente)
- `Instant` é o tipo correto para armazenar momentos absolutos

### **Bloco 2: Definir Timezone**
```java
ZoneId zonaSpSP = ZoneId.of("America/Sao_Paulo");
```
- Cria um `ZoneId` representando São Paulo (UTC-3)
- Nomenclatura `"America/Sao_Paulo"` é padrão IANA (reconhecida mundialmente)
- Evita valores hardcoded como `-03:00` (que não contabiliza horário de verão)

### **Bloco 3: Converter Instant ? LocalDateTime**
```java
LocalDateTime eventoLocal = LocalDateTime.ofInstant(instant, zonaSpSP);
```
- Transforma o `Instant` UTC em `LocalDateTime` respeitando o timezone
- `ofInstant()` = Instant + ZoneId ? LocalDateTime
- Resultado: data/hora legível no timezone solicitado

**Exemplo**: `2024-08-15T20:00:00Z` vira `2024-08-15T17:00:00` (São Paulo)

### **Bloco 4: Calcular Diferença**
```java
LocalDateTime agora = LocalDateTime.now(zonaSpSP);
long horasRestantes = ChronoUnit.HOURS.between(agora, eventoLocal);
```
- `LocalDateTime.now(zonaSpSP)` pega o momento atual **já** no timezone de SP
- `ChronoUnit.HOURS.between()` calcula quantas horas inteiras entre dois `LocalDateTime`
- Retorna `long` (pode ser negativo se o evento já passou)

### **Bloco 5: Formatar Data e Hora**
```java
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH");

String dataFormatada = eventoLocal.format(fmt);        // "15/08/2024"
String horaFormatada = eventoLocal.format(fmtHora);    // "17"
```
- Define padrões de formatação usando `ofPattern()`
- `dd/MM/yyyy` = dia/mês/ano com zeros à esquerda
- `HH` = hora em 24h com zero à esquerda
- `.format()` aplica o padrão ao `LocalDateTime`

### **Bloco 6: Montar String Final**
```java
String resultado = String.format(
    "%s às %sh (São Paulo)",
    dataFormatada,
    horaFormatada
);
```
- `String.format()` substitui `%s` pelos valores
- Resultado: `"15/08/2024 às 17h (São Paulo)"`

### **Bloco 7: Exibir**
```java
System.out.println("Evento: " + resultado);
System.out.println("Faltam " + horasRestantes + " horas");
```
- Imprime no console

---

## Saída Esperada
```
Evento: 15/08/2024 às 17h (São Paulo)
Faltam 42 horas
```
*(considerando que são~18 horas agora em SP)*

---

## Variações Úteis

### Outro Timezone
```java
ZoneId zoneRio = ZoneId.of("America/Rio_Branco");     // Acre (UTC-5)
ZoneId zoneLondres = ZoneId.of("Europe/London");       // UTC+0 ou +1
ZoneId zoneTokyoDe = ZoneId.of("Asia/Tokyo");          // UTC+9
```

### Apenas dias
```java
long diasRestantes = ChronoUnit.DAYS.between(agora, eventoLocal);
```

### Converter para Instant de novo
```java
Instant instante = eventoLocal.atZone(zonaSpSP).toInstant();
```

### Validar se passou
```java
if (horasRestantes < 0) {
    System.out.println("Evento passou há " + Math.abs(horasRestantes) + " horas");
}
```

---

## Checklist de Entendimento

- ? `Instant` = sempre UTC, imutável, para BD/API
- ? `LocalDateTime` = data/hora sem timezone, para exibição local
- ? `ZoneId` = fuso horário, evita hardcode de offsets
- ? `ChronoUnit` = operações de cálculo (diferenças, adições)
- ? `DateTimeFormatter` = conversão para string legível