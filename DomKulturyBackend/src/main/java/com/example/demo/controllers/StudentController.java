package com.example.demo.controllers;

import com.example.demo.dto.StudentDTO;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.Student;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/students")
public class StudentController {

    StudentService studentService;
    StudentMapper studentMapper;

    @Autowired
    public StudentController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getStudents() {
        return ResponseEntity.ok(studentMapper.toStudentsDTO(studentService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentMapper.toStudentDTO(studentService.findById(id)));
    }

    @PostMapping
    public Student addStudent(@Valid @RequestBody StudentDTO studentDTO) {
        return studentService.addStudent(studentMapper.toStudentAdd(studentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);

        return ResponseEntity.ok(new MessageResponse("Student deleted successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@Valid @RequestBody StudentDTO studentDTO, @PathVariable Long id) {
        studentService.updateStudent(studentDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(studentDTO);
    }
}
