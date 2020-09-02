package com.example.demo.exceptions;

public class UserAlreadyAddedToEventException extends RuntimeException {
    public UserAlreadyAddedToEventException(String username, Long eventId) {
        super("Username: " + username + ", is already added to event with id: " + eventId);
    }
}
