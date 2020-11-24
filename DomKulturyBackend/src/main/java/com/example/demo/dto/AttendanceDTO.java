package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class AttendanceDTO {

    private Long id;
    private Long lessonId;
    private Long studentId;
    private boolean present;
}
