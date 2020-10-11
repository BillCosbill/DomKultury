package com.example.demo.controllers;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.mappers.SubjectMapper;
import com.example.demo.models.Student;
import com.example.demo.models.Subject;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.interfaces.StudentService;
import com.example.demo.services.interfaces.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;
    private final StudentService studentService;

    @Autowired
    public SubjectController(SubjectService subjectService, SubjectMapper subjectMapper, StudentService studentService) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getSubjects() {
        return ResponseEntity.ok(subjectMapper.toSubjectsDTO(subjectService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubject(@PathVariable Long id) {
        return ResponseEntity.ok(subjectMapper.toSubjectDTO(subjectService.findById(id)));
    }

    @PostMapping
    public Subject addSubject(@RequestBody SubjectDTO subjectDTO) {
        return subjectService.addSubject(subjectDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);

        return ResponseEntity.ok(new MessageResponse("Subject deleted successfully!"));
    }

    @DeleteMapping("/{id}/deleteStudent/{studentId}")
    public ResponseEntity<MessageResponse> deleteStudentFromSubject(@PathVariable Long id, @PathVariable Long studentId) {
        subjectService.deleteStudentFromSubject(id, studentId);

        return ResponseEntity.ok(new MessageResponse("Student deleted successfully from subject!"));
    }

    @PostMapping("/{id}/addStudent")
    public ResponseEntity<MessageResponse> addStudentToSubject(@RequestBody Student student, @PathVariable Long id) {
        subjectService.addStudentToSubject(student, id);

        return ResponseEntity.ok(new MessageResponse("Student added successfully to subject"));
    }

    @PatchMapping("/{subjectId}/addStudentFromDatabase/{studentId}")
    public ResponseEntity<MessageResponse> addStudentFromDatabase(@PathVariable Long subjectId, @PathVariable Long studentId) {
        subjectService.addStudentFromDatabase(subjectId, studentId);

        return ResponseEntity.ok(new MessageResponse("Student added successfully to subject"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> updateEvent(@RequestBody SubjectDTO subjectDTO, @PathVariable Long id) {
        subjectService.updateSubject(subjectDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(subjectDTO);
    }

}
