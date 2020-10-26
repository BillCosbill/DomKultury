package com.example.demo.exceptions;

public class PeselInUseException extends RuntimeException {
    public PeselInUseException(String pesel) {
        super("Pesel: " + pesel + ", is already in use");
    }
}

