package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//TODO dodać wartość która określa czy obecność została sprawdzona, a dla każdej stworzonej lekcji robić attendance z domyślnymi wartościami present = false
public class AttendanceDTO {
    private Long id;
    private Long lessonId;
    private Long studentId;
    private boolean present;
}
