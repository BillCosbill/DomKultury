package com.example.demo.services.interfaces;

import com.example.demo.dto.LessonDTO;
import com.example.demo.models.Lesson;

import java.util.List;

public interface LessonService {
    List<Lesson> findAll();

    Lesson findById(Long id);

    void deleteLesson(Long id);

    Lesson updateLesson(LessonDTO lessonDTO, Long id);

    Lesson save(Lesson lesson);

    LessonDTO addLesson(LessonDTO lessonDTO);
}
