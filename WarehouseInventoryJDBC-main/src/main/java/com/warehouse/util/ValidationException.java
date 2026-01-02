package com.warehouse.util;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }

    public String toString() {
        return "ValidationException: " + getMessage();
    }
}