package com.example.demo.controllers;

import com.example.demo.models.EmailMessage;
import com.example.demo.services.implementations.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<EmailMessage> sendMail(@RequestBody EmailMessage emailMessage) {
        emailService.sendMail(emailMessage.getFromId(), emailMessage.getTo(), emailMessage.getSubject(), emailMessage
                .getText());
        return ResponseEntity.ok(emailMessage);
    }

    @PostMapping("/sendMultiple")
    public ResponseEntity<EmailMessage> sendMultipleMails(@RequestBody EmailMessage emailMessage) {
        emailService.sendMultipleMails(emailMessage.getFromId(), emailMessage.getSubject(), emailMessage
                .getText(), emailMessage.getStudentList());
        return ResponseEntity.ok(emailMessage);
    }
}
