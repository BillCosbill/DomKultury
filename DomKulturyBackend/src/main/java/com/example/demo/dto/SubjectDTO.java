package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubjectDTO {
    private Long id;
    @NotBlank(message = "Imie nie moze byc puste")
    private String name;
    @NotBlank(message = "Opis nie moze byc pusty")
    private String description;
    @NotNull(message = "Nauczyciel nie moze byc pusty")
    private Long teacherId;
    private List<Long> studentsId;
    private List<Long> lessonsId;
}
