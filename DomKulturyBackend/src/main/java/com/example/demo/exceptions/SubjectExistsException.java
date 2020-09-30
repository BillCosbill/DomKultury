package com.example.demo.exceptions;

public class SubjectExistsException extends RuntimeException {
    public SubjectExistsException(Long id) {
        super("Subject with id: " + id + ", already exists");
    }
}
