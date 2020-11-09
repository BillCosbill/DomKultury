package com.example.demo.exceptions;

import com.example.demo.annotations.HttpErrorCode;

@HttpErrorCode(404)
public class NotFoundGlobalException extends RuntimeException {

    public NotFoundGlobalException(String msg) {
        super(msg);
    }
}
