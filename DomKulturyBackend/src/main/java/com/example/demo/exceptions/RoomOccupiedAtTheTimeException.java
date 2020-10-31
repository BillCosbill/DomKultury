package com.example.demo.exceptions;

public class RoomOccupiedAtTheTimeException extends RuntimeException {
    public RoomOccupiedAtTheTimeException(String name) {
        super("Room " + name + " is occupied within the given time frame");
    }
}
