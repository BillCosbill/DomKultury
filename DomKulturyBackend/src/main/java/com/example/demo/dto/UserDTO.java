package com.example.demo.dto;

import com.example.demo.models.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Role> roles = new HashSet<>();
    private boolean isEnable;
    private List<Long> subjectsId;
}
