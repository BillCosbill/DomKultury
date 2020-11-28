package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @NotBlank(message = "Haslo nie moze byc puste")
    private String password;

    @NotNull(message = "Rola nie moze byc pusta")
    // TODO zmieniłem na EAGER bo testy nie działały
    //    @ManyToMany(fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @NotNull(message = "Aktywonosc nie moze byc pusta")
    private boolean isEnable;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher")
    private List<Subject> subjects;

    public User(String username, String firstName, String lastName, String email, String password, boolean isEnable) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isEnable = isEnable;
    }
}
