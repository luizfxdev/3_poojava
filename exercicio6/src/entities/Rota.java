package entities;

public class Rota {
    private String origem;
    private String destino;

    public Rota(String origem, String destino) {
        this.origem = origem;
        this.destino = destino;
    
    }
    public String getOrigem() {
        return origem;
    }
    public String getDestino() {
        return destino;
    }

    @Override 
    public String toString() {
        return origem + " → " + destino;
    }
    
}
