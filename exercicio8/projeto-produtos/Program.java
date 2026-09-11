import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        List<Product> products = new ArrayList<>();

        System.out.print("Enter the number of products: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 1; i <= n; i++) {
            System.out.println("\nProduct #" + i + " data:");

            System.out.print("Common, imported or used (c/i/u)? ");
            char type = sc.nextLine().charAt(0);

            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Price: ");
            double price = sc.nextDouble();
            sc.nextLine();

            if (type == 'i') {
                System.out.print("Customs fee: ");
                double customsFee = sc.nextDouble();
                sc.nextLine();
                products.add(new ImportedProduct(name, price, customsFee));

            } else if (type == 'u') {
                System.out.print("Manufacture date (dd/MM/yyyy): ");
                String dateStr = sc.nextLine();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(dateStr, dtf);
                products.add(new UsedProduct(name, price, date));

            } else {
                products.add(new CommonProduct(name, price));
            }
        }

        System.out.println("\nPRICE TAGS:");
        for (Product p : products) {
            System.out.println(p.priceTag());
        }

        sc.close();
    }
}