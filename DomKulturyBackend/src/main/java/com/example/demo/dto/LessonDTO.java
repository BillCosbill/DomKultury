package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LessonDTO {
    private Long id;
    private String topic;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private Long roomId;
    private List<Long> attendancesId;
    private Long subjectId;
}
