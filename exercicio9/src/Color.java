package com.example.shapes;

public enum Color {
    BLACK(0),
    BLUE(1),
    RED(2);

    private int value;

    Color(int value) {
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }
}