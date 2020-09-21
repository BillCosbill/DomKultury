package com.example.demo.services.interfaces;

import com.example.demo.dto.StudentDTO;
import com.example.demo.models.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();

    Student findById(Long id);

    void deleteStudent(Long id);

    Student updateStudent(StudentDTO studentDTO, Long id);

    Student save(Student student);

    Student addStudent(Student student);
}
