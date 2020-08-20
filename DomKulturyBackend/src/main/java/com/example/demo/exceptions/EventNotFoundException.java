package com.example.demo.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event with id: " + id + ", doesnt exists");
    }
}
