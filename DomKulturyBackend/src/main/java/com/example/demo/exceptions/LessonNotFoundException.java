package com.example.demo.exceptions;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(Long id) {
        super("Lesson with id: " + id + ", doesnt exists");
    }
}
