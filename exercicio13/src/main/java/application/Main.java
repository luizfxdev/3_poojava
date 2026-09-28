package application;

import java.util.Scanner;

import services.PrintService;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Qual o valor? ");
        int amount = Integer.parseInt(scanner.nextLine());

        PrintService<Integer> printService = new PrintService<>();

        for (int i = 0; i < amount; i++) {
            int value = Integer.parseInt(scanner.nextLine());
            printService.addValue(value);
        }

        printService.print();
        System.out.println("Primeiro: " + printService.first());

        scanner.close();
    }
}