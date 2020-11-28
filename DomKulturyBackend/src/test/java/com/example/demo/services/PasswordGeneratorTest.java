package com.example.demo.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordGeneratorTest {

    @Autowired
    PasswordGenerator passwordGenerator;

    @Test
    void generatePassword() {
        assertEquals(6 ,passwordGenerator.generatePassword(5).length());
        assertEquals(6 ,passwordGenerator.generatePassword(3).length());
        assertEquals(6 ,passwordGenerator.generatePassword(6).length());
        assertEquals(7 ,passwordGenerator.generatePassword(7).length());
    }
}
