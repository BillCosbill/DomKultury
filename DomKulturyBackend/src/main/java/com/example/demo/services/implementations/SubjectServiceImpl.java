package com.example.demo.services.implementations;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.exceptions.SubjectExistsException;
import com.example.demo.exceptions.SubjectNotFoundException;
import com.example.demo.mappers.SubjectMapper;
import com.example.demo.models.Subject;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.services.interfaces.LessonService;
import com.example.demo.services.interfaces.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    SubjectRepository subjectRepository;
    SubjectMapper subjectMapper;
    LessonService lessonService;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, SubjectMapper subjectMapper, LessonService lessonService) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
        this.lessonService = lessonService;
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject findById(Long id) {
        return subjectRepository.findById(id).orElseThrow(() -> new SubjectNotFoundException(id));
    }

    @Override
    public void deleteSubject(Long id) {
        Subject subject = findById(id);

        subject.getLessons().forEach(lesson -> {
            lessonService.deleteLesson(lesson.getId());
        });

        subjectRepository.delete(subject);
    }

    @Override
    public Subject updateSubject(SubjectDTO subjectDTO, Long id) {
        if (!subjectDTO.getId().equals(id)) {
            throw new SubjectExistsException(id);
        }

        Subject subject = subjectMapper.toSubject(subjectDTO, id);

        return save(subject);
    }

    @Override
    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject addSubject(Subject subject) {
        //TODO można sprawdzić czy nie istnieje już podany adres email

        return save(subject);
    }
}
