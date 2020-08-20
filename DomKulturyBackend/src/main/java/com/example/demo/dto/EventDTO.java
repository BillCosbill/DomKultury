package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private Long teacherId;
    private List<Long> studentsId;
    private int studentsLimit;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
}
