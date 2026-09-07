public class Motocicleta extends Veiculo {
    
    private int cilindrada;
    private double pesoMotocicleta;  
    
    public Motocicleta(String placa, double capacidadeKg, double velocidadeKmh, 
                       double custoPorKm, int cilindrada, double pesoMotocicleta) {
        super(placa, capacidadeKg, velocidadeKmh, custoPorKm);
        this.cilindrada = cilindrada;
        this.pesoMotocicleta = pesoMotocicleta;
    }
    
    @Override
    public double calcularCusto(double distancia) {
        double custoBase = custoPorKm * distancia;
        
        
        
        if (pesoMotocicleta > capacidadeKg) {
            custoBase += 100.0;  
        }
        
        return custoBase;
    }
    
    public int getCilindrada() {
        return cilindrada;
    }
    
    @Override
    public String toString() {
        return "Motocicleta{" +
                "placa='" + placa + '\'' +
                ", capacidadeKg=" + capacidadeKg +
                ", velocidadeKmh=" + velocidadeKmh +
                ", cilindrada=" + cilindrada +
                '}';
    }
}