package com.example.demo.dto;

import com.example.demo.models.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;
    @NotBlank(message = "Nazwa uzytkownika nie moze byc pusta")
    private String username;
    @NotBlank(message = "Imie nie moze byc puste")
    private String firstName;
    @NotBlank(message = "Nazwisko nie moze byc puste")
    private String lastName;
    @NotBlank(message = "Email nie moze byc pusty")
    @Email(message = "Email musi byc poprawny")
    private String email;
    @NotNull(message = "Rola nie moze byc pusta")
    private Set<Role> roles = new HashSet<>();
    @NotNull(message = "Aktywonosc nie moze byc pusta")
    private boolean isEnable;
    private List<Long> subjectsId;
}
