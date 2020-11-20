package com.example.demo.services.implementations;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.exceptions.ConflictGlobalException;
import com.example.demo.exceptions.NotFoundGlobalException;
import com.example.demo.mappers.SubjectMapper;
import com.example.demo.models.Attendance;
import com.example.demo.models.Student;
import com.example.demo.models.Subject;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final LessonService lessonService;
    private final StudentService studentService;
    private final AttendanceService attendanceService;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, SubjectMapper subjectMapper, LessonService lessonService, StudentService studentService, AttendanceService attendanceService) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
        this.lessonService = lessonService;
        this.studentService = studentService;
        this.attendanceService = attendanceService;
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject findById(Long id) {
        return subjectRepository.findById(id).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono przedmiotu o id " + id));
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
            throw new ConflictGlobalException("Wystąpił błąd. Identyfikator przedmiotu nie został rozpoznany!");
        }

        Subject subject = subjectMapper.toSubject(subjectDTO, id);

        return save(subject);
    }

    @Override
    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject addSubject(SubjectDTO subjectDTO) {
        Subject subject = subjectMapper.toSubjectAdd(subjectDTO);

        return save(subject);
    }

    @Override
    public void deleteStudentFromSubject(Long id, Long studentId) {
        Subject subject = findById(id);

        Student student = studentService.findById(studentId);

        subject.getStudents().remove(student);

        save(subject);
    }

    @Override
    public void addStudentToSubject(Student student, Long id) {
        studentService.addStudent(student);

        Subject subject = findById(id);
        subject.getStudents().add(student);

        subject.getLessons().forEach(lesson -> {
            attendanceService.addAttendance(new Attendance(lesson, student));
        });

        save(subject);
    }

    @Override
    public void addStudentFromDatabase(Long subjectId, Long studentId) {
        Subject subject = findById(subjectId);
        Student student = studentService.findById(studentId);

        if(subject.getStudents().contains(student)) {
            throw new ConflictGlobalException("Uczeń o indeksie " + studentId + " jest już zapisany na wskazany przedmiot.");
        }

        subject.getLessons().forEach(lesson -> {
            attendanceService.addAttendance(new Attendance(lesson, student));
        });

        subject.getStudents().add(student);
        save(subject);
    }
}
