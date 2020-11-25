package com.example.demo.controllers;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.LessonDTO;
import com.example.demo.dto.StudentAttendanceDTO;
import com.example.demo.mappers.LessonMapper;
import com.example.demo.models.Lesson;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.interfaces.LessonService;
import com.example.demo.services.interfaces.RoomService;
import com.example.demo.services.interfaces.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;
    private final RoomService roomService;
    private final SubjectService subjectService;

    @Autowired
    public LessonController(LessonService lessonService, LessonMapper lessonMapper, RoomService roomService, SubjectService subjectService) {
        this.lessonService = lessonService;
        this.lessonMapper = lessonMapper;
        this.roomService = roomService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<LessonDTO> getLessons() {
        return lessonMapper.toLessonsDTO(lessonService.findAll());
    }

    @GetMapping("/{id}/studentAttendances")
    public List<StudentAttendanceDTO> getStudentAttendances(@PathVariable Long id) {
        return lessonService.getStudentAttendance(id);
    }

    @GetMapping("/{id}")
    public LessonDTO getLesson(@PathVariable Long id) {
        return lessonMapper.toLessonDTO(lessonService.findById(id));
    }

    @PostMapping
    public LessonDTO addLesson(@Valid @RequestBody LessonDTO lessonDTO) {
        Lesson lesson = new Lesson();
        lesson.setTopic(lessonDTO.getTopic());
        lesson.setDescription(lessonDTO.getDescription());
        lesson.setStartDate(LocalDateTime.parse(lessonDTO.getStartDate()));
        lesson.setFinishDate(LocalDateTime.parse(lessonDTO.getFinishDate()));
        lesson.setRoom(roomService.findById(lessonDTO.getRoomId()));
        lesson.setSubject(subjectService.findById(lessonDTO.getSubjectId()));

        Lesson newLesson = lessonService.addLesson(lesson);

        return lessonMapper.toLessonDTO(newLesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);

        return ResponseEntity.ok(new MessageResponse("Lekcja o id " + id + " została usunięta!"));
    }

    @PutMapping("/{id}")
    public LessonDTO updateEvent(@Valid @RequestBody LessonDTO lessonDTO, @PathVariable Long id) {
        lessonService.updateLesson(lessonDTO, id);
        return lessonDTO;
    }

    @PatchMapping("/{lessonId}/checkAttendance")
    public ResponseEntity<MessageResponse> checkAttendance(@RequestBody List<AttendanceDTO> attendanceDTOS, @PathVariable Long lessonId) {
        lessonService.checkAttendance(attendanceDTOS, lessonId);
        return ResponseEntity.ok(new MessageResponse("Attendance checked"));
    }
}
