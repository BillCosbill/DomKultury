package com.example.demo.services.implementations;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.LessonDTO;
import com.example.demo.dto.StudentAttendanceDTO;
import com.example.demo.exceptions.*;
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
import com.example.demo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    LessonRepository lessonRepository;
    LessonMapper lessonMapper;

    AttendanceRepository attendanceRepository;
    AttendanceMapper attendanceMapper;
    SubjectRepository subjectRepository;
    private final StudentService studentService;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, LessonMapper lessonMapper, AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper, SubjectRepository subjectRepository, StudentService studentService) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
        this.subjectRepository = subjectRepository;
        this.studentService = studentService;
    }

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson findById(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono lekcji z id " + id));
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
            throw new ConflictGlobalException("Lekcja z id " + id + " już istnieje");
        }

        Lesson lesson = lessonMapper.toLesson(lessonDTO, id);

        return save(lesson);
    }

    @Override
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson addLesson(LessonDTO lessonDTO) {

        Lesson lesson = lessonMapper.toLessonAdd(lessonDTO);

        Optional<Lesson> lessonOpt = findAll().stream().filter(anyLesson ->
                (lesson.getRoom() == anyLesson.getRoom() && lesson.getStartDate().isAfter(anyLesson.getStartDate()) && lesson.getStartDate().isBefore(anyLesson.getFinishDate())) ||
                (lesson.getRoom() == anyLesson.getRoom() && lesson.getFinishDate().isAfter(anyLesson.getStartDate()) && lesson.getFinishDate().isBefore(anyLesson.getFinishDate())))
                                              .findAny();

        if (lessonOpt.isPresent()) {
            throw new ConflictGlobalException("Pokój o numerze " + lesson.getRoom().getNumber() + " jest zajęty od " + lesson.getStartDate() + " do " + lesson.getFinishDate());
        }

        save(lesson);

        Subject subject = subjectRepository.findById(lessonDTO.getSubjectId()).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono przedmiotu o id " + lessonDTO.getSubjectId()));

        List<Student> students = subject.getStudents();

        students.forEach(student -> {
            attendanceRepository.save(new Attendance(lesson, student));
        });

        return lesson;
    }

    @Override
    public void checkAttendance(List<AttendanceDTO> attendanceDTOS, Long lessonId) {
        attendanceDTOS.forEach(x -> {
            attendanceRepository.save(attendanceMapper.toAttendanceAdd(x));
        });

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono lekcji o id " + lessonId));

        save(lesson);
    }

    @Override
    public List<StudentAttendanceDTO> getStudentAttendance(Long lessonId) {
        Lesson lesson = findById(lessonId);

        List<StudentAttendanceDTO> studentAttendanceDTOs = new ArrayList<>();

        lesson.getAttendances().forEach(attendance -> {
            StudentAttendanceDTO toAdd = new StudentAttendanceDTO();
            Student student = studentService.findById(attendance.getStudent().getId());
            toAdd.setId(student.getId());
            toAdd.setFirstName(student.getFirstName());
            toAdd.setLastName(student.getLastName());
            toAdd.setEmail(student.getEmail());
            toAdd.setBirthday(student.getBirthday().toString());
            toAdd.setPresent(attendance.isPresent());

            studentAttendanceDTOs.add(toAdd);
        });

        return studentAttendanceDTOs;
    }

}
