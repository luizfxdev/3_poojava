package votacao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LeitorRegistros {

    private static final String SEPARADOR = ",";

    public List<RegistroVotacao> ler(Path arquivo) throws IOException {
        return Files.readAllLines(arquivo).stream()
                .filter(linha -> !linha.isBlank())
                .map(this::converter)
                .toList();
    }

    private RegistroVotacao converter(String linha) {
        String[] campos = linha.split(SEPARADOR);
        if (campos.length != 2) {
            throw new IllegalArgumentException("Linha inválida: " + linha);
        }
        String candidato = campos[0].trim();
        int votos = Integer.parseInt(campos[1].trim());
        return new RegistroVotacao(candidato, votos);
    }
}
