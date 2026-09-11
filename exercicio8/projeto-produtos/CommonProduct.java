public class CommonProduct extends Product {

    public CommonProduct(String name, double price) {
        super(name, price);
    }

    @Override
    public String priceTag() {
        return String.format("%s - $ %.2f", name, price);
    }
}