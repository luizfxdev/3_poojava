package com.example.logistica;

public class QuantidadeInsuficienteException extends Exception {
    public QuantidadeInsuficienteException(String mensagem) {
        super(mensagem);
    }
}