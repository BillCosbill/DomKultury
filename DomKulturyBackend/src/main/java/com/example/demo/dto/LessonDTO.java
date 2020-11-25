package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LessonDTO {

    private Long id;

    @NotBlank(message = "Temat nie moze byc pusty")
    private String topic;

    @NotBlank(message = "Opis nie moze byc pusty")
    private String description;

    @NotBlank(message = "Data rozpoczecia nie moze byc pusta")
    private String startDate;

    @NotBlank(message = "Data zakonczenia nie moze byc pusta")
    private String finishDate;

    @NotNull(message = "Pokoj nie moze byc pusty")
    private Long roomId;

    private List<Long> attendancesId;

    @NotNull(message = "Przedmiot nie moze byc pusty")
    private Long subjectId;
}
