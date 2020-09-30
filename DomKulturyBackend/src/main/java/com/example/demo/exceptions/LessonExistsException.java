package com.example.demo.exceptions;

public class LessonExistsException extends RuntimeException {
    public LessonExistsException(Long id) {
        super("Lesson with id: " + id + ", already exists");
    }
}
