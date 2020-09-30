package com.example.demo.exceptions;

public class StudentExistsException extends RuntimeException {
    public StudentExistsException(Long id) {
        super("Student with id: " + id + ", already exists");
    }
}
