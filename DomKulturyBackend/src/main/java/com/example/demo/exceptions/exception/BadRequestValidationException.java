package com.example.demo.exceptions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BadRequestValidationException extends RuntimeException {

    public BadRequestValidationException(String msg) {
        super(msg);
    }
}
