package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebAppApplicationTests {

    @Test
    void contextLoads() {
        Long a = new Long(1);
        long b = a;

        assertEquals(1L, b);
    }

}
