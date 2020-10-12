package com.example.demo.exceptions;

//TODO Exception zamiast RuntimeException
public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException(Long id) {
        super("Subject with id: " + id + ", doesnt exists");
    }
}
