package com.example.demo.exceptions.advisor;

import com.example.demo.annotations.HttpErrorCode;
import com.example.demo.exceptions.*;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

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

        status = clazz.getAnnotation(HttpErrorCode.class).value();

        return HttpStatus.valueOf(status);
    }
}


