package com.example.demo.mappers;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.exceptions.TestException;
import com.example.demo.models.Attendance;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceMapper {
    AttendanceRepository attendanceRepository;
    LessonRepository lessonRepository;
    StudentRepository studentRepository;

    @Autowired
    public AttendanceMapper(AttendanceRepository attendanceRepository, LessonRepository lessonRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
    }

    public List<AttendanceDTO> toAttendancesDTO(List<Attendance> attendances) {
        return ((List<AttendanceDTO>) attendances.stream().map(this::toAttendanceDTO).collect(Collectors.toList()));
    }

    public AttendanceDTO toAttendanceDTO(Attendance attendance) {
        AttendanceDTO attendanceDTO = new AttendanceDTO();

        attendanceDTO.setId(attendance.getId());
        attendanceDTO.setLessonId(attendance.getLesson().getId());
        attendanceDTO.setStudentId(attendance.getStudent().getId());
        attendanceDTO.setPresent(attendance.isPresent());

        return attendanceDTO;
    }

    public Attendance toAttendance(AttendanceDTO attendanceDTO, Long id) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new TestException());

        if (attendanceDTO.getId() != null) {
            attendance.setId(attendanceDTO.getId());
        }
        if (attendanceDTO.getLessonId() != null) {
            attendance.setLesson(lessonRepository.findById(attendanceDTO.getLessonId())
                                                 .orElseThrow(() -> new TestException()));
        }
        if (attendanceDTO.getStudentId() != null) {
            attendance.setStudent(studentRepository.findById(attendanceDTO.getStudentId())
                                                   .orElseThrow(() -> new TestException()));
        }

        //TODO możliwe że obecność będzie wariowała przez tą linijkę, bo nie sprawdzam czy w attendanceDTO została zmieniona lub poadana wartość present
        attendance.setPresent(attendanceDTO.isPresent());

        return attendance;
    }

    public Attendance toAttendanceAdd(AttendanceDTO attendanceDTO) {
        Attendance attendance = new Attendance();

        if (attendanceDTO.getId() != null) {
            attendance.setId(attendanceDTO.getId());
        }
        if (attendanceDTO.getLessonId() != null) {
            attendance.setLesson(lessonRepository.findById(attendanceDTO.getLessonId())
                                                 .orElseThrow(() -> new TestException()));
        }
        if (attendanceDTO.getStudentId() != null) {
            attendance.setStudent(studentRepository.findById(attendanceDTO.getStudentId())
                                                   .orElseThrow(() -> new TestException()));
        }

        //TODO możliwe że obecność będzie wariowała przez tą linijkę, bo nie sprawdzam czy w attendanceDTO została zmieniona lub poadana wartość present
        attendance.setPresent(attendanceDTO.isPresent());

        return attendance;
    }


}
