package com.example.demo.exceptions;

public class EventStudentsLimitHasBeenReachedException extends RuntimeException {
    public EventStudentsLimitHasBeenReachedException(Long id) {
        super("Event with id: " + id + ", has reached students limit");
    }
}
