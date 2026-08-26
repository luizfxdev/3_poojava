package exercicio3;

public class Hospedagem {
    private Campeao[] quartos;
    private static final int TOTAL_QUARTOS = 10;

    public Hospedagem() {
        this.quartos = new Campeao[TOTAL_QUARTOS];

        for (int i = 0; i < TOTAL_QUARTOS; i++) {
            this.quartos[i] = null;
        }

    }

    public void registrarCampeao(String nome, String funcao, int numeroQuarto) {
        if (numeroQuarto < 0 || numeroQuarto >= TOTAL_QUARTOS) {
            System.out.println("Erro: Quarto inválido! Escolha entre 0 e 9.");
            return;
        }

        if (quartos[numeroQuarto] != null) {
            System.out.printf("Erro: Quarto %d já está ocupado por %s%n",
                    numeroQuarto, quartos[numeroQuarto].getNome());
            return;
        }

        Campeao campeao = new Campeao(nome, funcao, numeroQuarto);
        quartos[numeroQuarto] = campeao;
        System.out.printf("✅ %s alocado(a) no quarto %d (%s)%n",
                nome, numeroQuarto, funcao);
    }

    public void exibirTodos() {
        System.out.println("\n========== STATUS DOS QUARTOS ==========\n");
        for (int i = 0; i < TOTAL_QUARTOS; i++) {
            if (quartos[i] == null) {
                System.out.printf("Quarto %d: [VAZIO]%n", i);
            } else {

                System.out.printf("Quarto %d: %s (%s)%n",
                        i,
                        quartos[i].getNome(),
                        quartos[i].getFuncao());
            }
        }
        System.out.println("\n========================================\n");
    }

    public int contarOcupados() {
        int contador = 0;

        for (int i = 0; i < TOTAL_QUARTOS; i++) {
            if (quartos[i] != null) {
                contador++;
            }
        }

        return contador;
    }
}


    
    