package com.example.demo.exceptions;

public class StudentExistsException extends RuntimeException {
    public StudentExistsException(Long id) {
        super("Student with id: " + id + ", already exists");
    }
    public StudentExistsException(String pesel) {
        super("Student with pesel: " + pesel + ", already exists");
    }
}
