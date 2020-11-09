package com.example.demo.exceptions;

import com.example.demo.annotations.HttpErrorCode;

@HttpErrorCode(409)
public class ConflictGlobalException extends RuntimeException {
    public ConflictGlobalException(String msg) {
        super(msg);
    }
}
