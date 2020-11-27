package com.example.demo.controllers;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.exceptions.NotFoundGlobalException;
import com.example.demo.mappers.SubjectMapper;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.models.Subject;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.interfaces.StudentService;
import com.example.demo.services.interfaces.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;
    private final StudentService studentService;
    private final UserRepository userRepository;

    @Autowired
    public SubjectController(SubjectService subjectService, SubjectMapper subjectMapper, StudentService studentService, UserRepository userRepository) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
        this.studentService = studentService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<SubjectDTO> getSubjects() {
        return subjectMapper.toSubjectsDTO(subjectService.findAll());
    }

    @GetMapping("/{id}")
    public SubjectDTO getSubject(@PathVariable Long id) {
        return subjectMapper.toSubjectDTO(subjectService.findById(id));
    }

    @PostMapping
    public SubjectDTO addSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setName(subjectDTO.getName());
        subject.setDescription(subjectDTO.getDescription());
        subject.setTeacher(userRepository.findById(subjectDTO.getTeacherId()).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono uzytkownika o id " + subjectDTO.getTeacherId())));

        Subject newSubject = subjectService.addSubject(subject);

        return subjectMapper.toSubjectDTO(newSubject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);

        return ResponseEntity.ok(new MessageResponse("Przemiot o id " + id + " zostal usuniety"));
    }

    @DeleteMapping("/{id}/deleteStudent/{studentId}")
    public ResponseEntity<MessageResponse> deleteStudentFromSubject(@PathVariable Long id, @PathVariable Long studentId) {
        subjectService.deleteStudentFromSubject(id, studentId);

        return ResponseEntity.ok(new MessageResponse("Usunieto ucznia z przedmiotu"));
    }

    @PostMapping("/{id}/addStudent")
    public ResponseEntity<MessageResponse> addStudentToSubject(@RequestBody Student student, @PathVariable Long id) {
        subjectService.addStudentToSubject(student, id);

        return ResponseEntity.ok(new MessageResponse("Dodano nowego ucznia do bazy oraz do przedmiotu"));
    }

    @PatchMapping("/{subjectId}/addStudentFromDatabase/{studentId}")
    public ResponseEntity<MessageResponse> addStudentFromDatabase(@PathVariable Long subjectId, @PathVariable Long studentId) {
        subjectService.addStudentFromDatabase(subjectId, studentId);

        return ResponseEntity.ok(new MessageResponse("Dodano ucznia z bazy do przedmiotu"));
    }

    @PutMapping("/{id}")
    public SubjectDTO updateSubject(@RequestBody SubjectDTO subjectDTO, @PathVariable Long id) {
        subjectService.updateSubject(subjectDTO, id);
        return subjectDTO;
    }

}
