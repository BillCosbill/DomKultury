package com.example.demo.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with id: " + id + ", doesnt exists");
    }
}
