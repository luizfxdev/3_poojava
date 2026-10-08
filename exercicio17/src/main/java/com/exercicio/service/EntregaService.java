package com.exercicio.service;

import com.exercicio.model.Entrega;

import java.util.Comparator;
import java.util.List;

public class EntregaService {

    public List<Entrega> ordenarPorFreteDecrescente(List<Entrega> entregas) {
        return entregas.stream()
                .sorted(Comparator.comparingDouble(Entrega::getFrete).reversed())
                .toList();
    }

    public double calcularFreteTotal(List<Entrega> entregas) {
        return entregas.stream()
                .mapToDouble(Entrega::getFrete)
                .sum();
    }
}