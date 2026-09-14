package com.example.shapes;

public class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(Color color, double widht, double height) {
        super(color);
        this.widht = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }
}