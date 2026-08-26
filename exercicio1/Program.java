import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) {
        
        try (Scanner sc = new Scanner(System.in)) {
            
            // Lê os lados do triângulo X
            System.out.println("Enter the measures of triangle X:");
            double a1 = sc.nextDouble();
            double b1 = sc.nextDouble();
            double c1 = sc.nextDouble();
            
            // Cria o objeto Triangle X
            Triangle x = new Triangle(a1, b1, c1);
            
            // Lê os lados do triângulo Y
            System.out.println("Enter the measures of triangle Y:");
            double a2 = sc.nextDouble();
            double b2 = sc.nextDouble();
            double c2 = sc.nextDouble();
            
            // Cria o objeto Triangle Y
            Triangle y = new Triangle(a2, b2, c2);
            
            // Calcula as áreas
            double areaX = x.area();
            double areaY = y.area();
            
            // Imprime as áreas formatadas
            System.out.printf("Triangle X area: %.4f%n", areaX);
            System.out.printf("Triangle Y area: %.4f%n", areaY);
            
            // Compara e mostra qual tem maior área
            if (areaX > areaY) {
                System.out.println("Larger area: X");
            }
            else if (areaY > areaX) {
                System.out.println("Larger area: Y");
            }
            else {
                System.out.println("Larger area: Equal");
            }
        }
    }
}