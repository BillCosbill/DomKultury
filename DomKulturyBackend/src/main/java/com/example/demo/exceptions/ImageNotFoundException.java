package com.example.demo.exceptions;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(Long id) {
        super("Image with id: " + id + ", not found");
    }
}
