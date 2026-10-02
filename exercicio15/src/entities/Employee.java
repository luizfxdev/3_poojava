package entities;

public class Employee {

    private final String name;
    private final String email;
    private final Double salary;

    public Employee(String name, String email, Double salary) {
        this.name = name;
        this.email = email;
        this.salary = salary;
    }

    public static Employee fromCsvLine(String line) {
        String[] fields = line.split(",");
        String name = fields[0].trim();
        String email = fields[1].trim();
        Double salary = Double.parseDouble(fields[2].trim());
        return new Employee(name, email, salary);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Double getSalary() {
        return salary;
    }
}