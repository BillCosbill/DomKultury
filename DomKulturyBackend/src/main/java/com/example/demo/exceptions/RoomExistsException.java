package com.example.demo.exceptions;

public class RoomExistsException extends RuntimeException {
    public RoomExistsException(String number) {
        super("Room with number: " + number + ", already exists");
    }
}
