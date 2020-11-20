package com.example.demo.exceptions.advisor;

import com.example.demo.annotations.HttpErrorCode;
import lombok.var;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());

        StringBuilder message = new StringBuilder();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            message.append(error.getDefaultMessage() + ". ");
        }

        body.put("message", message);

        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handle(Exception e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", e.getMessage());

        var httpStatus = getStatusFromException(e.getClass());

        return new ResponseEntity<>(body, httpStatus);
    }

    private HttpStatus getStatusFromException(Class<? extends Exception> clazz) {
        // default HttpStatus.INTERNAL_SERVER_ERROR = 500
        int status = 500;

        if (clazz.getAnnotation(HttpErrorCode.class) != null) {
            status = clazz.getAnnotation(HttpErrorCode.class).value();
        }

        return HttpStatus.valueOf(status);
    }
}


