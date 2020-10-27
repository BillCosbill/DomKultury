package com.example.demo.exceptions;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Incorrect password");
    }
}
