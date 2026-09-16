package com.example.logistica;

public class CapacidadeExcedidaException extends Exception {
    public CapacidadeExcedidaException(String mensagem) {
        super(mensagem);
    }
}