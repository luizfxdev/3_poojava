package exercicio3;

public class Campeao {
    private String nome;
    private String funcao;
    private int quarto;

    public Campeao(String nome, String funcao, int quarto) {
        this.nome = nome;
        this.quarto = quarto;
        this.funcao = funcao;
    }

    public String getNome() {
        return nome;
    }

     public String getFuncao() {
        return funcao;                      
    }
 
    public int getQuarto() {
        return quarto;                      
    }
 
    
    public void exibir() {
        System.out.println("=== Campeão ===");
        System.out.printf("Nome: %s%n", nome);           
        System.out.printf("Função: %s%n", funcao);       
        System.out.printf("Quarto: %d%n", quarto);       
        System.out.println();
    }
}