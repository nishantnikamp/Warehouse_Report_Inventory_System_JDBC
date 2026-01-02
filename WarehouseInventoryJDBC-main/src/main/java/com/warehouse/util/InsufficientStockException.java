package com.warehouse.util;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }

    public String toString() {
        return "InsufficientStockException: " + getMessage();
    }
}