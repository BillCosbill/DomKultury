package com.example.demo.controllers;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.mappers.AttendanceMapper;
import com.example.demo.models.Attendance;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.interfaces.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, AttendanceMapper attendanceMapper) {
        this.attendanceService = attendanceService;
        this.attendanceMapper = attendanceMapper;
    }

    @GetMapping
    public List<AttendanceDTO> getAttendance() {
        return attendanceMapper.toAttendancesDTO(attendanceService.findAll());
    }

    @GetMapping("/{id}")
    public AttendanceDTO getAttendance(@PathVariable Long id) {
        return attendanceMapper.toAttendanceDTO(attendanceService.findById(id));
    }

    @PostMapping
    public AttendanceDTO addAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        Attendance attendance = attendanceMapper.toAttendanceAdd(attendanceDTO);
        attendanceService.addAttendance(attendance);
        return getAttendance(attendance.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);

        return ResponseEntity.ok(new MessageResponse("Attendance deleted successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDTO> updateAttendance(@RequestBody AttendanceDTO attendanceDTO, @PathVariable Long id) {
        attendanceService.updateAttendance(attendanceDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(attendanceDTO);
    }
}
