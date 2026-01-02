package com.warehouse.util;

public class ActiveDamageReportsException extends Exception {
    public ActiveDamageReportsException(String message) {
        super(message);
    }

    public String toString() {
        return "ActiveDamageReportsException: " + getMessage();
    }
}