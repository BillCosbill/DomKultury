package com.example.demo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Imie nie moze byc puste")
    private String firstName;

    @NotBlank(message = "Nazwisko nie moze byc puste")
    private String lastName;

    @NotBlank(message = "Email nie moze byc pusty")
    @Email(message = "Email musi byc poprawny")
    private String email;

    @NotNull(message = "Data urodzin nie moze byc pusta")
    private LocalDate birthday;

    public Student(String firstName, String lastName, String email, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
    }
}
