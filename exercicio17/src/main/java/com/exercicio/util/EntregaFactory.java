package com.exercicio.util;

import com.exercicio.model.Entrega;

import java.util.List;

public class EntregaFactory {

    public static List<Entrega> criarEntregas() {
        return List.of(
                new Entrega("Ana Lima", "São Paulo", 2.5, 35.00),
                new Entrega("Carlos Melo", "Rio de Janeiro", 10.0, 80.00),
                new Entrega("Beatriz Souza", "Belo Horizonte", 1.2, 20.00),
                new Entrega("Diego Ramos", "Curitiba", 5.0, 55.00),
                new Entrega("Fernanda Cruz", "Recife", 8.3, 70.00),
                new Entrega("Gabriel Neves", "Salvador", 3.7, 45.00)
        );
    }
}