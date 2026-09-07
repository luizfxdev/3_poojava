public class Caminhao extends Veiculo {
    
    private int eixos;
    private double cargaAtual;
    
    public Caminhao(String placa, double capacidadeKg, double velocidadeKmh, 
                    double custoPorKm, int eixos) {
        super(placa, capacidadeKg, velocidadeKmh, custoPorKm);
        this.eixos = eixos;
        this.cargaAtual = 0;
    }
    
    @Override
    public double calcularCusto(double distancia) {
        double custoBase = custoPorKm * distancia;
        
       
        double custoTonelada = (cargaAtual / 1000.0) * 50.0;  
        
        
        double custoEixo = eixos * 30.0;  
        
        return custoBase + custoTonelada + custoEixo;
    }
    
    public void carregarCarga(double peso) {
        if (cargaAtual + peso <= capacidadeKg) {
            cargaAtual += peso;
        } else {
            System.out.println("Erro: capacidade excedida!");
        }
    }
    
    public double getCargaAtual() {
        return cargaAtual;
    }
    
    @Override
    public String toString() {
        return "Caminhao{" +
                "placa='" + placa + '\'' +
                ", capacidadeKg=" + capacidadeKg +
                ", velocidadeKmh=" + velocidadeKmh +
                ", cargaAtual=" + cargaAtual +
                ", eixos=" + eixos +
                '}';
    }
}