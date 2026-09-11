public class ImportedProduct extends Product {

    private double customsFee;

    public ImportedProduct(String name, double price, double customsFee) {
        super(name, price);
        this.customsFee = customsFee;
    }

    public double getCustomsFee() {
        return customsFee;
    }

    @Override
    public String priceTag() {
        double finalPrice = price + customsFee;
        return String.format("%s - $ %.2f (Customs fee: $ %.2f)",
                name, finalPrice, customsFee);
    }
}