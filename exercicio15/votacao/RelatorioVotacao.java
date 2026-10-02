package votacao;

import java.util.Comparator;
import java.util.Map;

public class RelatorioVotacao {

    public void imprimir(Map<String, Integer> totais) {
        System.out.println("RELATÓRIO CONSOLIDADO DE VOTAÇÃO");
        totais.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entrada -> System.out.printf("%-20s %d%n", entrada.getKey(), entrada.getValue()));
        System.out.printf("%-20s %d%n", "TOTAL", somar(totais));
    }

    private int somar(Map<String, Integer> totais) {
        return totais.values().stream().mapToInt(Integer::intValue).sum();
    }
}
