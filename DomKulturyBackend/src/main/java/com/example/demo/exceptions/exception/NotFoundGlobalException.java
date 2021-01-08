package com.example.demo.exceptions.exception;

import com.example.demo.annotations.HttpErrorCode;

@HttpErrorCode(404)
public class NotFoundGlobalException extends RuntimeException {
    public NotFoundGlobalException(String msg) {
        super(msg);
    }
}
