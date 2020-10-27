package com.example.demo.exceptions;

public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException(Long id) {
        super("Subject with id: " + id + ", doesnt exists");
    }
}
