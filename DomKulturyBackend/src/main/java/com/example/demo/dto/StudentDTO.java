package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class StudentDTO {

    private Long id;

    @NotBlank(message = "Imie nie moze byc puste")
    private String firstName;

    @NotBlank(message = "Nazwisko nie moze byc puste")
    private String lastName;

    @NotBlank(message = "Pesel nie moze byc pusty")
    @Size(min = 11, max = 11, message = "Pesel musi skladac sie z 11 znakow")
    private String pesel;

    @NotBlank(message = "Email nie moze byc pusty")
    @Email(message = "Email musi byc poprawny")
    private String email;

    @NotNull(message = "Data urodzin nie moze byc pusta")
    private String birthday;
}
