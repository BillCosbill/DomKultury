package com.example.demo.exceptions;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(Long id) {
        super("Room with id: " + id + ", doesnt exists");
    }
}
