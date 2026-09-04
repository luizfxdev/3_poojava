package entities.enums;

public enum TipoVeiculo {

    CAMINHÃO(0.00),
    VAN(0.20),
    MOTO(0.40);

    private final Double desconto;

    
TipoVeiculo(Double desconto) {
    this.desconto = desconto;
    
    }

public Double getDesconto() {
    return desconto;
    }

}
