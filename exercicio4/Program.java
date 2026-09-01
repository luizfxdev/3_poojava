import java.util.Locale;
import java.util.Scanner;

public class Program{

    public static void main(String[] args){
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner (System.in);

        System.out.println("Quantos funcionários serão registrados?");
        int n = sc.nextInt();

        List<Employee> list = new ArrayList<>();

        for (int i = 1; i <= n; i++){
            System.out.println();
            System.out.println("Employee #" + i + ":");

            System.out.println("Id: ");
            int id = sc.nextInt();

            System.out.println("Nome:");
            sc.nextLine();
            String name = sc.nextLine;

            System.out.println("Salário");
            double salary = sc.nextDouble();

            list.add(new Employee(id, name, salary));
        }

        System.out.println();
        System.out.println("Insira o ID do funcionário que terá aumento salarial: ");
        int searchId = sc.nextInt();

        Employee emp = null;
        for (Employee e : list) {
            if (e.getId() == searchId) {
                emp = e;
                break;
            }
        }

        if (emp != null){
            System.out.println("Insira a porcentagem: ");
            double percentage = sc.nextDouble();
            emp.increaseSalary(percentage);
        } else {
            System.out.println("Este id não existe!");

        }
        
        System.out.println();
        System.out.println("Lista de funcionários:");
        for (Employee e : list) {
            System.out.println(e);
        }
    sc.close();
    }
    
}
