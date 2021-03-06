package com.example.demo.mappers;

import com.example.demo.dto.StudentDTO;
import com.example.demo.exceptions.exception.NotFoundGlobalException;
import com.example.demo.models.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentMapper {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentMapper(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDTO> toStudentsDTO(List<Student> students) {
        return ((List<StudentDTO>) students.stream()
                                           .map(this::toStudentDTO)
                                           .collect(Collectors.toList()));
    }

    public StudentDTO toStudentDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setId(student.getId());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setBirthday(student.getBirthday().toString());

        return studentDTO;
    }

    public Student toStudent(StudentDTO studentDTO, Long id) {
        Student student = studentRepository.findById(id)
                                           .orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono ucznia z id " + id));

        if (studentDTO.getFirstName() != null) {
            student.setFirstName(studentDTO.getFirstName());
        }
        if (studentDTO.getLastName() != null) {
            student.setLastName(studentDTO.getLastName());
        }
        if (studentDTO.getEmail() != null) {
            student.setEmail(studentDTO.getEmail());
        }
        if (studentDTO.getBirthday() != null) {
            student.setBirthday(LocalDate.parse(studentDTO.getBirthday()));
        }

        return student;
    }
}
