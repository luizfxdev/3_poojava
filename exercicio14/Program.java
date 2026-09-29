import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Program {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Set<Integer> alunosDoInstrutor = new HashSet<>();

            for (String curso : new String[]{"A", "B", "C"}) {
                alunosDoInstrutor.addAll(lerAlunoDoCurso(scanner, curso));
            }

            System.out.println("Total de estudantes " + alunosDoInstrutor.size());
        }
    }

    private static Set<Integer> lerAlunosDoCurso(Scanner scanner, String curso) {
        System.out.println("Quantos alunos no curso " + curso + "? ");
        int quantidade = scanner.nextInt();

        Set<Integer> alunos = new HashSet<>();
        for (int i = 0; i < quantidade; i++) {
            alunos.add(scanner.nextInt());
        }
        return alunos;
    }
    
}
