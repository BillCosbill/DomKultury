package com.example.demo.exceptions;

public class AttendanceNotFoundException extends RuntimeException {
    public AttendanceNotFoundException(Long id) {
        super("Attendance with id: " + id + ", doesnt exists");
    }
}
