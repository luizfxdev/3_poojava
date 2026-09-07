public class Van extends Veiculo {
    
    private int portas;
    private double consumoLitroKm;
    private double precoGasolinaPorLitro;
    
    public Van(String placa, double capacidadeKg, double velocidadeKmh, 
               double custoPorKm, int portas, double consumoLitroKm, 
               double precoGasolinaPorLitro) {
        super(placa, capacidadeKg, velocidadeKmh, custoPorKm);
        this.portas = portas;
        this.consumoLitroKm = consumoLitroKm;
        this.precoGasolinaPorLitro = precoGasolinaPorLitro;
    }
    
    @Override
    public double calcularCusto(double distancia) {
        double custoBase = custoPorKm * distancia;
        
        // Custo adicional de combustível
        double litrosNecessarios = distancia * consumoLitroKm;
        double custoCombustivel = litrosNecessarios * precoGasolinaPorLitro;
        
        return custoBase + custoCombustivel;
    }
    
    public int getPortas() {
        return portas;
    }
    
    public double getConsumoPorKm() {
        return consumoLitroKm;
    }
    
    @Override
    public String toString() {
        return "Van{" +
                "placa='" + placa + '\'' +
                ", capacidadeKg=" + capacidadeKg +
                ", velocidadeKmh=" + velocidadeKmh +
                ", portas=" + portas +
                ", consumoLitroKm=" + consumoLitroKm +
                '}';
    }
}