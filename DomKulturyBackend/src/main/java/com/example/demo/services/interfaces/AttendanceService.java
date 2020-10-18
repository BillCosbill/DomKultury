package com.example.demo.services.interfaces;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.models.Attendance;

import java.util.List;

public interface AttendanceService {
    List<Attendance> findAll();

    Attendance findById(Long id);

    void deleteAttendance(Long id);

    Attendance updateAttendance(AttendanceDTO attendanceDTO, Long id);

    Attendance save(Attendance attendance);

    Attendance addAttendance(Attendance attendance);
}
