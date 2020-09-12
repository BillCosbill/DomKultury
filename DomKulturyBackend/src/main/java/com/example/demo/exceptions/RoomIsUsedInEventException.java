package com.example.demo.exceptions;

import com.example.demo.models.ERole;

public class RoomIsUsedInEventException extends RuntimeException {
    public RoomIsUsedInEventException(Long id) {
        super("Room with id: " + id + ", is used at least by one of Events");
    }
}
