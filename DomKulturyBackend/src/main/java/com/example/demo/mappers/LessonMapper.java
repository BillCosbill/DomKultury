package com.example.demo.mappers;

import com.example.demo.dto.LessonDTO;
import com.example.demo.exceptions.*;
import com.example.demo.models.Attendance;
import com.example.demo.models.Lesson;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonMapper {
    LessonRepository lessonRepository;
    StudentRepository studentRepository;
    RoomRepository roomRepository;
    SubjectRepository subjectRepository;
    AttendanceRepository attendanceRepository;

    @Autowired
    public LessonMapper(LessonRepository lessonRepository, StudentRepository studentRepository, RoomRepository roomRepository,  SubjectRepository subjectRepository, AttendanceRepository attendanceRepository) {
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
        this.subjectRepository = subjectRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<LessonDTO> toLessonsDTO(List<Lesson> lessons) {
        return ((List<LessonDTO>) lessons.stream().map(this::toLessonDTO).collect(Collectors.toList()));
    }

    public LessonDTO toLessonDTO(Lesson lesson) {
        LessonDTO lessonDTO = new LessonDTO();

        lessonDTO.setId(lesson.getId());
        lessonDTO.setTopic(lesson.getTopic());
        lessonDTO.setDescription(lesson.getDescription());

        lessonDTO.setStartDate(lesson.getStartDate().toString());
        lessonDTO.setFinishDate(lesson.getFinishDate().toString());

        lessonDTO.setRoomId(lesson.getRoom().getId());

        if (lesson.getAttendances() != null) {
            List<Long> attendancesId = new ArrayList<>();
            lesson.getAttendances().forEach(x -> attendancesId.add(x.getId()));
            lessonDTO.setAttendancesId(attendancesId);
        }

        lessonDTO.setSubjectId(lesson.getSubject().getId());

        return lessonDTO;
    }

    public Lesson toLesson(LessonDTO lessonDTO, Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono lekcji o id " + id));

        if (lessonDTO.getId() != null) {
            lesson.setId(lessonDTO.getId());
        }
        if (lessonDTO.getTopic() != null) {
            lesson.setTopic(lessonDTO.getTopic());
        }
        if (lessonDTO.getDescription() != null) {
            lesson.setDescription(lessonDTO.getDescription());
        }
        if (lessonDTO.getStartDate() != null) {
            lesson.setStartDate(LocalDateTime.parse(lessonDTO.getStartDate()));
        }
        if (lessonDTO.getFinishDate() != null) {
            lesson.setFinishDate(LocalDateTime.parse(lessonDTO.getFinishDate()));
        }
        if (lessonDTO.getRoomId() != null) {
            lesson.setRoom(roomRepository.findById(lessonDTO.getRoomId()).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono sali o id " + lessonDTO.getRoomId())));
        }
        if (lessonDTO.getAttendancesId() != null) {
            List<Attendance> attendances = new ArrayList<>();
            lessonDTO.getAttendancesId().forEach(attendanceId -> attendances
                    .add(attendanceRepository.findById(attendanceId).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono obiektu frekwencji o id " + attendanceId))));
            lesson.setAttendances(attendances);
        }

        if (lessonDTO.getSubjectId() != null) {
            lesson.setSubject(subjectRepository.findById(lessonDTO.getSubjectId()).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono przedmiotu o id " + lessonDTO.getSubjectId())));
        }

        return lesson;
    }
}
