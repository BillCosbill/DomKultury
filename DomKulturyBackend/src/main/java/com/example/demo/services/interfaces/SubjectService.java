package com.example.demo.services.interfaces;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.models.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAll();

    Subject findById(Long id);

    void deleteSubject(Long id);

    Subject updateSubject(SubjectDTO subjectDTO, Long id);

    Subject save(Subject subject);

    Subject addSubject(Subject subject);
}
