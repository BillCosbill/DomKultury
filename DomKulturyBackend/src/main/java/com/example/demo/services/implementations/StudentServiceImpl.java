package com.example.demo.services.implementations;

import com.example.demo.dto.StudentDTO;
import com.example.demo.exceptions.EmailInUseException;
import com.example.demo.exceptions.PeselInUseException;
import com.example.demo.exceptions.StudentExistsException;
import com.example.demo.exceptions.StudentNotFoundException;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.Student;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final  StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final  SubjectRepository subjectRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper, SubjectRepository subjectRepository, AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.subjectRepository = subjectRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
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
            throw new PeselInUseException(student.getPesel());
        }

        if(studentRepository.existsByEmail(student.getEmail())) {
            throw new EmailInUseException(student.getEmail());
        }

        if(userRepository.existsByPesel(student.getPesel())) {
            throw new PeselInUseException(student.getPesel());
        }

        if(userRepository.existsByEmail(student.getEmail())) {
            throw new EmailInUseException(student.getEmail());
        }

        return save(student);
    }
}
