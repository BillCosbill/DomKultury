package com.example.demo.controllers;

import com.example.demo.dto.EventDTO;
import com.example.demo.mappers.EventMapper;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.interfaces.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @Autowired
    public EventController(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getEvents() {
        return ResponseEntity.ok(eventMapper.toEventsDTO(eventService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventMapper.toEventDTO(eventService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        eventService.deleteEvent(id);

        return ResponseEntity.ok(new MessageResponse("Event deleted successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO, @PathVariable Long id) {
        eventService.updateEvent(eventDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(eventDTO);
    }
}
