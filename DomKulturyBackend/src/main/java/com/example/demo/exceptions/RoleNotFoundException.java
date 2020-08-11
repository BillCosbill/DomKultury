package com.example.demo.exceptions;

import com.example.demo.models.ERole;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(ERole role) {
        super("Role: " + role + ", not found");
    }
}
