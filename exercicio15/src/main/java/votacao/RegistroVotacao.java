package votacao;

public record RegistroVotacao(String candidato, int votos) {

    public RegistroVotacao {
        if (candidato == null || candidato.isBlank()) {
            throw new IllegalArgumentException("Nome do candidato é obrigatório");
        }
        if (votos < 0) {
            throw new IllegalArgumentException("Quantidade de votos não pode ser negativa");
        }
    }
}
