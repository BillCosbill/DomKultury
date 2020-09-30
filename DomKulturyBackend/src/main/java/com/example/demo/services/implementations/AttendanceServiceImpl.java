package com.example.demo.services.implementations;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.exceptions.AttendanceExistsException;
import com.example.demo.exceptions.AttendanceNotFoundException;
import com.example.demo.mappers.AttendanceMapper;
import com.example.demo.models.Attendance;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.services.interfaces.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    AttendanceRepository attendanceRepository;
    AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance findById(Long id) {
        return attendanceRepository.findById(id).orElseThrow(() -> new AttendanceNotFoundException(id));
    }

    @Override
    public void deleteAttendance(Long id) {
        Attendance attendance = findById(id);
        attendanceRepository.delete(attendance);
    }

    @Override
    public Attendance updateAttendance(AttendanceDTO attendanceDTO, Long id) {
        if (!attendanceDTO.getId().equals(id)) {
            throw new AttendanceExistsException(id);
        }

        Attendance attendance = attendanceMapper.toAttendance(attendanceDTO, id);

        return save(attendance);
    }

    @Override
    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public AttendanceDTO addAttendance(AttendanceDTO attendanceDTO) {
        save(attendanceMapper.toAttendanceAdd(attendanceDTO));
        return attendanceDTO;
    }
}
