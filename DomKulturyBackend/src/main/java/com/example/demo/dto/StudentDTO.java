package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class StudentDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private String email;
    private LocalDate birthday;
}
