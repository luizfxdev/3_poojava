package com.example.shapes;

public abstract class Shape {
    protected Color color;

    public SHape(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract double area ();
    
}