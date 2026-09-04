# Exercício: Sistema de Gestão de Fretes

## 🚚 Enunciado

Desenvolver um sistema de gestão de fretes para uma transportadora que permita:

1. **Registrar fretes** com status, tipo de veículo, rota de origem/destino e valor
2. **Gerenciar o ciclo de vida** do frete (PENDING → IN_TRANSIT → DELIVERED)
3. **Cancelar fretes** em caso de necessidade
4. **Calcular custos** com base no tipo de veículo e distância
5. **Exibir sumário** de fretes por status

---

## 📋 Requisitos

### Dados do Frete
- ID único
- Data do frete (quando foi criado)
- Status (PENDENTE, EM_TRANSITO, ENTREGUE, CANCELADO)
- Tipo de veículo (CAMINHAO, VAN, MOTO)
- Rota (origem e destino)
- Distância em km
- Valor base por km
- Cliente responsável

### Funcionalidades
- Criar novo frete
- Alterar status do frete
- Cancelar frete
- Calcular valor total (distância × valor por km)
- Aplicar desconto por tipo de veículo
- Listar fretes por status

---

## 🎯 Cenário de Uso

```
Sistema de Fretes - Transportadora XYZ
=====================================

Frete #1:
  Cliente: João Silva
  Rota: São Paulo → Rio de Janeiro
  Distância: 400 km
  Tipo: CAMINHAO
  Status: PENDENTE
  Valor: R$ 2.000,00

Frete #2:
  Cliente: Maria Santos
  Rota: Curitiba → São Paulo
  Distância: 400 km
  Tipo: MOTO
  Status: EM_TRANSITO
  Valor: R$ 1.200,00 (desconto de 40%)

Frete #3:
  Cliente: Pedro Costa
  Rota: Belo Horizonte → Brasília
  Distância: 700 km
  Tipo: VAN
  Status: ENTREGUE
  Valor: R$ 2.800,00 (desconto de 20%)

RELATÓRIO POR STATUS:
- PENDENTE: 1 frete(s) - Total: R$ 2.000,00
- EM_TRANSITO: 1 frete(s) - Total: R$ 1.200,00
- ENTREGUE: 1 frete(s) - Total: R$ 2.800,00
- CANCELADO: 0 frete(s) - Total: R$ 0,00
```

---

## 🏗️ Estrutura de Classes

```
entities/
├── Rota.java
├── Cliente.java
└── Frete.java

enums/
├── StatusFrete.java
└── TipoVeiculo.java

application/
└── Program.java
```

---

## 💡 Dicas de Implementação

1. **Enum StatusFrete**: PENDENTE, EM_TRANSITO, ENTREGUE, CANCELADO
2. **Enum TipoVeiculo**: CAMINHAO (desconto 0%), VAN (desconto 20%), MOTO (desconto 40%)
3. **Classe Rota**: armazena origem e destino
4. **Classe Cliente**: armazena nome e email
5. **Classe Frete**: composição com Cliente e Rota, gerencia status
6. **Método de cálculo**: `valorTotal() = distancia × valorPorKm × (1 - desconto)`

---

## ✅ Conceitos POO Envolvidos

- **Encapsulamento**: getters e setters controlados
- **Composição**: Frete "tem" Cliente e Rota
- **Enums**: Status e tipos imutáveis e seguros
- **ArrayList**: Lista de fretes
- **Polimorfismo**: método `toString()` customizado
- **Métodos**: cálculos de desconto e total
- **Data**: uso de `Date` e `SimpleDateFormat`

---

## 📖 Próximos Passos

1. Implementar as 2 enums
2. Implementar as 3 classes (Cliente, Rota, Frete)
3. Implementar o programa principal (entrada de dados)
4. Exibir relatório final

Consulte os markdowns de **Resolução** e **Arquitetura** para detalhes de implementação.