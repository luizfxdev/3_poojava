package votacao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        Path arquivo = Path.of(args.length > 0 ? args[0] : "dados/votos.csv");

        List<RegistroVotacao> registros = new LeitorRegistros().ler(arquivo);
        Map<String, Integer> totais = new ConsolidadorVotos().consolidar(registros);

        new RelatorioVotacao().imprimir(totais);
    }
}
