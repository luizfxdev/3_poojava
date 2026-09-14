package com.example.shapes;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of shapes: ");
        int n = sc.nextInt();
        sc.nextLine();

        List<Shape> shapes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("\nShape #" + (i + 1) + " data:");
            System.out.print("Retangle or Circle (r/c)? ");
            char type = sc.next().charAt(0);

            System.out.print("Color (BLACK/BLUE/RED): ");
            String colorStr = sc.next().toUpperCase();
            Color color = Color.valueOf(colorStr);

            Shape shape;
        }

        System.out.println("\nSHAPE AREAS:");
        for (Shape shape : shapes){
            System.out.println(String.format("%.2f", shape.area())));
        }

        sc.close();
    }
}