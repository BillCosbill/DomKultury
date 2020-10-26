package com.example.demo.email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public ResponseEntity<EmailMessage> sendMail(@RequestBody EmailMessage emailMessage) throws MessagingException {
        emailService.sendMail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getText());

        return ResponseEntity.ok(emailMessage);
    }
}
