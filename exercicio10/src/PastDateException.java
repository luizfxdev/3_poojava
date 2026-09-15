package com.example.reservation;

public class PastDateException extends Exception {
    public PastDateException(String message) {
        super(message);
    }
}