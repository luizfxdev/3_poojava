package com.example.logistica;

public class EnderecoPallet {
    private int numero;
    private String codigoPallet;
    private double quantidade;
    private double capacidadeMaxima;
    
    public EnderecoPallet(int numero, String codigoPallet, double quantidade, double capacidadeMaxima) 
            throws CapacidadeExcedidaException {
        validar(quantidade, capacidadeMaxima);
        this.numero = numero;
        this.codigoPallet = codigoPallet;
        this.quantidade = quantidade;
        this.capacidadeMaxima = capacidadeMaxima;
    }
    
    public void armazenar(double valor) throws CapacidadeExcedidaException {
        double novaQuantidade = this.quantidade + valor;
        validarCapacidade(novaQuantidade);
        this.quantidade = novaQuantidade;
    }
    
    public void retirar(double valor) throws QuantidadeInsuficienteException {
        if (valor > this.quantidade) {
            throw new QuantidadeInsuficienteException(
                "Quantidade insuficiente no endereço. Disponível: " + this.quantidade
            );
        }
        this.quantidade -= valor;
    }
    
    private void validar(double quantidade, double capacidadeMaxima) 
            throws CapacidadeExcedidaException {
        if (quantidade < 0) {
            throw new CapacidadeExcedidaException("Quantidade não pode ser negativa");
        }
        if (quantidade > capacidadeMaxima) {
            throw new CapacidadeExcedidaException(
                "Quantidade excede a capacidade máxima do endereço. Capacidade: " + capacidadeMaxima
            );
        }
    }
    
    private void validarCapacidade(double novaQuantidade) throws CapacidadeExcedidaException {
        if (novaQuantidade > this.capacidadeMaxima) {
            throw new CapacidadeExcedidaException(
                "Armazenamento excederia a capacidade. Espaço disponível: " + 
                (this.capacidadeMaxima - this.quantidade)
            );
        }
    }
    
    public double obterEspacoDisponivel() {
        return capacidadeMaxima - quantidade;
    }
    
    public double obterTaxaOcupacao() {
        return (quantidade / capacidadeMaxima) * 100;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Endereço: %d%nCódigo do Pallet: %s%nQuantidade: %.2f%nCapacidade Máxima: %.2f%nEspaço Disponível: %.2f%nTaxa de Ocupação: %.2f%%",
            numero, codigoPallet, quantidade, capacidadeMaxima, obterEspacoDisponivel(), obterTaxaOcupacao()
        );
    }
}