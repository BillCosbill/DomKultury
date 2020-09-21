package com.example.demo.controllers;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.mappers.SubjectMapper;
import com.example.demo.models.Subject;
import com.example.demo.payload.response.MessageResponse;
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

    @Autowired
    public SubjectController(SubjectService subjectService, SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
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
    public Subject addSubject(@RequestBody Subject subject) {
        return subjectService.addSubject(subject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);

        return ResponseEntity.ok(new MessageResponse("Subject deleted successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> updateEvent(@RequestBody SubjectDTO subjectDTO, @PathVariable Long id) {
        subjectService.updateSubject(subjectDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(subjectDTO);
    }

}
