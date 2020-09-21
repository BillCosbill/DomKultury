package com.example.demo.services.implementations;

import com.example.demo.dto.LessonDTO;
import com.example.demo.exceptions.TestException;
import com.example.demo.mappers.LessonMapper;
import com.example.demo.models.Lesson;
import com.example.demo.repository.LessonRepository;
import com.example.demo.services.interfaces.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    LessonRepository lessonRepository;
    LessonMapper lessonMapper;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson findById(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new TestException());
    }

    @Override
    public void deleteLesson(Long id) {
        Lesson lesson = findById(id);
        lessonRepository.delete(lesson);
    }

    @Override
    public Lesson updateLesson(LessonDTO lessonDTO, Long id) {
        if (!lessonDTO.getId().equals(id)) {
            throw new TestException();
        }

        Lesson lesson = lessonMapper.toLesson(lessonDTO, id);

        return save(lesson);
    }

    @Override
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public LessonDTO addLesson(LessonDTO lessonDTO) {


        //TODO można sprawdzić czy nie istnieje już coś
        save(lessonMapper.toLessonAdd(lessonDTO));
        return lessonDTO;
    }

}
