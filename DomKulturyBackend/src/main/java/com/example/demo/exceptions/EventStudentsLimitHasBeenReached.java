package com.example.demo.exceptions;

public class EventStudentsLimitHasBeenReached extends RuntimeException {
    public EventStudentsLimitHasBeenReached(Long id) {
        super("Event with id: " + id + ", has reached students limit");
    }
}
