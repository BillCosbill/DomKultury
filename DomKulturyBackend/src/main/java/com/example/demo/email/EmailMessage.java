package com.example.demo.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String text;

    public EmailMessage(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}
