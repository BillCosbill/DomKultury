package com.example.demo.exceptions;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student with id: " + id + ", doesnt exists");
    }
}
