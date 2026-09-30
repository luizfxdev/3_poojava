package votacao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsolidadorVotos {

    public Map<String, Integer> consolidar(List<RegistroVotacao> registros) {
        Map<String, Integer> totais = new HashMap<>();
        for (RegistroVotacao registro : registros) {
            totais.merge(registro.candidato(), registro.votos(), Integer::sum);
        }
        return totais;
    }
}
