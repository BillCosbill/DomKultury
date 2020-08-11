package com.example.demo.services.interfaces;

import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest);

    void registerUser(SignupRequest signUpRequest);
}
