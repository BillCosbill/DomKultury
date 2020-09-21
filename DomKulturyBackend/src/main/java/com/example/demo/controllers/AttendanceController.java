package com.example.demo.controllers;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.LessonDTO;
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
public class AttendanceController  {

    private final AttendanceService attendanceService;
    private final AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, AttendanceMapper attendanceMapper) {
        this.attendanceService = attendanceService;
        this.attendanceMapper = attendanceMapper;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDTO>> getAttendance() {
        return ResponseEntity.ok(attendanceMapper.toAttendancesDTO(attendanceService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDTO> getAttendance(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceMapper.toAttendanceDTO(attendanceService.findById(id)));
    }

    @PostMapping
    public AttendanceDTO addAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.addAttendance(attendanceDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);

        return ResponseEntity.ok(new MessageResponse("Attendance deleted successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDTO> updateEvent(@RequestBody AttendanceDTO attendanceDTO, @PathVariable Long id) {
        attendanceService.updateAttendance(attendanceDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(attendanceDTO);
    }
}
