package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentAttendanceDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;

    private boolean present;
}
