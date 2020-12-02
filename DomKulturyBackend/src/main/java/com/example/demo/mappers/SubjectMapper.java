package com.example.demo.mappers;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.exceptions.exception.NotFoundGlobalException;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.models.Subject;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectMapper {

    SubjectRepository subjectRepository;
    UserRepository userRepository;
    StudentRepository studentRepository;
    LessonRepository lessonRepository;

    @Autowired
    public SubjectMapper(SubjectRepository subjectRepository, UserRepository userRepository, StudentRepository studentRepository, LessonRepository lessonRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<SubjectDTO> toSubjectsDTO(List<Subject> subjects) {
        return ((List<SubjectDTO>) subjects.stream().map(this::toSubjectDTO).collect(Collectors.toList()));
    }

    public SubjectDTO toSubjectDTO(Subject subject) {
        SubjectDTO subjectDTO = new SubjectDTO();

        subjectDTO.setId(subject.getId());
        subjectDTO.setName(subject.getName());
        subjectDTO.setDescription(subject.getDescription());

        if(subject.getTeacher() != null){
            subjectDTO.setTeacherId(subject.getTeacher().getId());
        }

        if(subject.getStudents() != null) {
            List<Long> studentsId = new ArrayList<>();
            subject.getStudents().forEach(x -> studentsId.add(x.getId()));
            subjectDTO.setStudentsId(studentsId);
        }

        if(subject.getLessons() != null) {
            List<Long> lessonsId = new ArrayList<>();
            subject.getLessons().forEach(x -> lessonsId.add(x.getId()));
            subjectDTO.setLessonsId(lessonsId);
        }

        return subjectDTO;
    }

    public Subject toSubject(SubjectDTO subjectDTO, Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono przedmiotu z id " + id));

        if (subjectDTO.getName() != null) {
            subject.setName(subjectDTO.getName());
        }
        if (subjectDTO.getDescription() != null) {
            subject.setDescription(subjectDTO.getDescription());
        }
        if (subjectDTO.getTeacherId() != null) {
            subject.setTeacher(userRepository.findById(subjectDTO.getTeacherId())
                                             .orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono użytkownika z id " + subject.getTeacher().getId())));
        }
        if (subjectDTO.getStudentsId() != null) {
            List<Student> students = new ArrayList<>();
            subjectDTO.getStudentsId()
                      .forEach(studentId -> students.add(studentRepository.findById(studentId).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono ucznia z id " + studentId))));
            subject.setStudents(students);
        }
        if (subjectDTO.getLessonsId() != null) {
            List<Lesson> lessons = new ArrayList<>();
            subjectDTO.getLessonsId()
                      .forEach(lessonId -> lessons.add(lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono lekcji z id " + lessonId))));
            subject.setLessons(lessons);
        }

        return subject;
    }

    public Subject toSubjectAdd(SubjectDTO subjectDTO) {
        Subject subject = new Subject();

        if (subjectDTO.getName() != null) {
            subject.setName(subjectDTO.getName());
        }
        if (subjectDTO.getDescription() != null) {
            subject.setDescription(subjectDTO.getDescription());
        }
        if (subjectDTO.getTeacherId() != null) {
            subject.setTeacher(userRepository.findById(subjectDTO.getTeacherId())
                                             .orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono użytkownika z id " + subject.getTeacher().getId())));
        }
        if (subjectDTO.getStudentsId() != null) {
            List<Student> students = new ArrayList<>();
            subjectDTO.getStudentsId()
                      .forEach(studentId -> students.add(studentRepository.findById(studentId).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono użytkownika z id " + studentId))));
            subject.setStudents(students);
        }
        if (subjectDTO.getLessonsId() != null) {
            List<Lesson> lessons = new ArrayList<>();
            subjectDTO.getLessonsId()
                      .forEach(lessonId -> lessons.add(lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono lekcji z id " + lessonId))));
            subject.setLessons(lessons);
        }

        return subject;
    }
}
