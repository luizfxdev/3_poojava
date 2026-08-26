public class Triangle {

    private double a;
    private double b;
    private double c;

    // Construtor
    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    // Método para calcular a área usando a fórmula de Heron
    public double area() {
        double p = (a + b + c) / 2.0;
        double area = Math.sqrt(p * (p - a) * (p - b) * (p - c));
        return area;
    }

    // Getters
    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }
}