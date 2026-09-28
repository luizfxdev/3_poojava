package services;

import java.util.ArrayList;
import java.util.List;

public class PrintService<T> {
    
    private final List<T> values = new ArrayList<>();

    public void addValue(T value) {
        values.add(value);
    }

    public T first() {
        return values.get(0);
    }

    public void print() {
        System.out.println(values);
    }
}