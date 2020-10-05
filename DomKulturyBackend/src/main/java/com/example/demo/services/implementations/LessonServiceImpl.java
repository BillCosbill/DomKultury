package com.example.demo.services.implementations;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.LessonDTO;
import com.example.demo.exceptions.LessonExistsException;
import com.example.demo.exceptions.LessonNotFoundException;
import com.example.demo.exceptions.SubjectNotFoundException;
import com.example.demo.mappers.AttendanceMapper;
import com.example.demo.mappers.LessonMapper;
import com.example.demo.models.Attendance;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.models.Subject;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.services.interfaces.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    LessonRepository lessonRepository;
    LessonMapper lessonMapper;

    AttendanceRepository attendanceRepository;
    AttendanceMapper attendanceMapper;
    SubjectRepository subjectRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, LessonMapper lessonMapper, AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper, SubjectRepository subjectRepository) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson findById(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new LessonNotFoundException(id));
    }

    @Override
    public void deleteLesson(Long id) {
        Lesson lesson = findById(id);

        lesson.getAttendances().forEach(attendance -> {
            attendanceRepository.delete(attendance);
        });

        lessonRepository.delete(lesson);
    }

    @Override
    public Lesson updateLesson(LessonDTO lessonDTO, Long id) {
        if (!lessonDTO.getId().equals(id)) {
            throw new LessonExistsException(id);
        }

        Lesson lesson = lessonMapper.toLesson(lessonDTO, id);

        return save(lesson);
    }

    @Override
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public LessonDTO addLesson(LessonDTO lessonDTO) {

        //TODO można sprawdzić czy nie istnieje już coś
        Lesson lesson = lessonMapper.toLessonAdd(lessonDTO);

        save(lesson);

        Subject subject = subjectRepository.findById(lessonDTO.getSubjectId()).orElseThrow(() -> new SubjectNotFoundException(lessonDTO.getSubjectId()));

        List<Student> students = subject.getStudents();

        students.forEach(student -> {
            attendanceRepository.save(new Attendance(lesson, student));
        });

        return lessonDTO;
    }

    @Override
    public void checkAttendance(List<AttendanceDTO> attendanceDTOS, Long lessonId) {
        attendanceDTOS.forEach(x -> {
            attendanceRepository.save(attendanceMapper.toAttendanceAdd(x));
        });

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonNotFoundException(lessonId));

        lesson.setAttendanceChecked(true);
        save(lesson);
    }

}
