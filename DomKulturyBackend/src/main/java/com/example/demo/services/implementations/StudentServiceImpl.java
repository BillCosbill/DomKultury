package com.example.demo.services.implementations;

import com.example.demo.dto.StudentDTO;
import com.example.demo.exceptions.StudentExistsException;
import com.example.demo.exceptions.StudentNotFoundException;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.Student;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    StudentRepository studentRepository;
    StudentMapper studentMapper;
    SubjectRepository subjectRepository;
    AttendanceRepository attendanceRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper,  SubjectRepository subjectRepository, AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.subjectRepository = subjectRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = findById(id);

        attendanceRepository.findAll().forEach(attendance -> {
            if(attendance.getStudent() == student) {
                attendanceRepository.delete(attendance);
            }
        });

        subjectRepository.findAll().forEach(subject -> {
            subject.getStudents().remove(student);
        });

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
        if(studentRepository.existsByPesel(student.getPesel())) {
            throw new StudentExistsException(student.getPesel());
        }

        return save(student);
    }
}
