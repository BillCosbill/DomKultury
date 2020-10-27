package com.example.demo.exceptions;

public class UserAssignedToSubjectException extends RuntimeException {
    public UserAssignedToSubjectException() {
        super("This user is assigned to at least one subject");
    }
}
