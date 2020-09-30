package com.example.demo.services.implementations;

import com.example.demo.dto.StudentDTO;
import com.example.demo.exceptions.StudentExistsException;
import com.example.demo.exceptions.StudentNotFoundException;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    StudentRepository studentRepository;
    StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        //TODO exception zmienić
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = findById(id);
        studentRepository.delete(student);
    }

    @Override
    public Student updateStudent(StudentDTO studentDTO, Long id) {
        if(!studentDTO.getId().equals(id)) {
            //TODO exception
            throw new StudentExistsException(id);
        }

        Student student = studentMapper.toStudent(studentDTO, id);

        return save(student);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student addStudent(Student student) {
        //TODO można sprawdzić czy nie istnieje już podany adres email

        return save(student);
    }
}
