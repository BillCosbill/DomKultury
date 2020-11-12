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

    @NotNull
    private String username;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String pesel;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @NotNull
    private boolean isEnable;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher")
    private List<Subject> subjects;

    public User(String username, String firstName, String lastName, String pesel, String email, String password, boolean isEnable) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.email = email;
        this.password = password;
        this.isEnable = isEnable;
    }
}
