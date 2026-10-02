package application;

import entities.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter full file path: ");
            String path = scanner.nextLine();

            System.out.print("Enter salary: ");
            double minimumSalary = scanner.nextDouble();

            List<Employee> employees = readEmployees(path);

            printEmailsAboveSalary(employees, minimumSalary);
            printSumOfSalariesByInitial(employees, 'M');
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static List<Employee> readEmployees(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(Employee::fromCsvLine)
                    .toList();
        }
    }

    private static void printEmailsAboveSalary(List<Employee> employees, double minimumSalary) {
        System.out.printf("E-mail de pessoas cujo salário é superior a %.2f:%n", minimumSalary);

        employees.stream()
                .filter(employee -> employee.getSalary() > minimumSalary)
                .map(Employee::getEmail)
                .sorted()
                .forEach(System.out::println);
    }

    private static void printSumOfSalariesByInitial(List<Employee> employees, char initial) {
        double sum = employees.stream()
                .filter(employee -> employee.getName().charAt(0) == initial)
                .mapToDouble(Employee::getSalary)
                .sum();

        System.out.printf("Soma dos salários das pessoas cujo nome começa com '%c': %.2f%n", initial, sum);
    }
}