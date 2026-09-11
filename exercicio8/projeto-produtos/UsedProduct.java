import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UsedProduct extends Product {

    private LocalDate manufactureDate;

    public UsedProduct(String name, double price, LocalDate manufactureDate) {
        super(name, price);
        this.manufactureDate = manufactureDate;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    @Override
    public String priceTag() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = manufactureDate.format(formatter);
        return String.format("%s - $ %.2f (Manufacture date: %s)",
                name, price, formattedDate);
    }
}