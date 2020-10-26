package com.example.demo.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeRequest {
    String password;
    String newPassword;

    public PasswordChangeRequest(String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }
}
