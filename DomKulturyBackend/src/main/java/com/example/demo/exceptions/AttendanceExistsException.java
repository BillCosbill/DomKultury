package com.example.demo.exceptions;

public class AttendanceExistsException extends RuntimeException {
    public AttendanceExistsException(Long id) {
        super("Attendance with id: " + id+ ", already exists");
    }
}

