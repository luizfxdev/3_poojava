package br.com.empresa.contratos.entrada;

public class EntradaInvalidaException extends RuntimeException {

    public EntradaInvalidaException(String mensagem) {
        super(mensagem);
    }

    public EntradaInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}