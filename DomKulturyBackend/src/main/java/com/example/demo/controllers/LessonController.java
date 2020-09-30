package com.example.demo.controllers;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.EventDTO;
import com.example.demo.dto.LessonDTO;
import com.example.demo.mappers.LessonMapper;
import com.example.demo.models.Lesson;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.interfaces.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @Autowired
    public LessonController(LessonService lessonService, LessonMapper lessonMapper) {
        this.lessonService = lessonService;
        this.lessonMapper = lessonMapper;
    }

    @GetMapping
    public ResponseEntity<List<LessonDTO>> getLessons() {
        return ResponseEntity.ok(lessonMapper.toLessonsDTO(lessonService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonMapper.toLessonDTO(lessonService.findById(id)));
    }

    @PostMapping
    public LessonDTO addLesson(@RequestBody LessonDTO lessonDTO) {
        return lessonService.addLesson(lessonDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);

        return ResponseEntity.ok(new MessageResponse("Lesson deleted successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateEvent(@RequestBody LessonDTO lessonDTO, @PathVariable Long id) {
        lessonService.updateLesson(lessonDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(lessonDTO);
    }

    @PatchMapping("/{lessonId}/checkAttendance")
    public ResponseEntity<MessageResponse> checkAttendance(@RequestBody List<AttendanceDTO> attendanceDTOS, @PathVariable Long lessonId) {
        lessonService.checkAttendance(attendanceDTOS, lessonId);
        return ResponseEntity.ok(new MessageResponse("Attendance checked"));
    }
}
