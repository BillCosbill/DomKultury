package com.example.demo.mappers;

import com.example.demo.dto.EventDTO;
import com.example.demo.exceptions.EventNotFoundException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventMapper {
    EventRepository eventRepository;
    UserRepository userRepository;

    @Autowired
    public EventMapper(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<EventDTO> toEventsDTO(List<Event> events) {
        return ((List<EventDTO>) events.stream().map(this::toEventDTO).collect(Collectors.toList()));
    }

    public EventDTO toEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();

        eventDTO.setId(event.getId());
        eventDTO.setTitle(event.getTitle());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setTeacherId(event.getTeacher().getId());

        List<Long> studentsId = new ArrayList<>();
        event.getStudents().forEach(x -> studentsId.add(x.getId()));
        eventDTO.setStudentsId(studentsId);

        eventDTO.setStudentsLimit(event.getStudentsLimit());
        eventDTO.setStartDate(event.getStartDate());
        eventDTO.setFinishDate(event.getFinishDate());

        return eventDTO;
    }

    public Event toEvent(EventDTO eventDTO, Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));

        if (eventDTO.getId() != null) {
            event.setId(eventDTO.getId());
        }
        if (eventDTO.getTitle() != null) {
            event.setTitle(eventDTO.getTitle());
        }
        if (eventDTO.getDescription() != null) {
            event.setDescription(eventDTO.getDescription());
        }
        if (eventDTO.getTeacherId() != null) {
            event.setTeacher(userRepository.findById(eventDTO.getTeacherId()).orElseThrow(() -> new UserNotFoundException(eventDTO.getTeacherId())));
        }
        if (eventDTO.getStudentsId() != null) {
            List<User> students = new ArrayList<>();
            eventDTO.getStudentsId().forEach(x -> students.add(userRepository.findById(x).orElseThrow(() -> new UserNotFoundException(x))));
            event.setStudents(students);
        }
        if (eventDTO.getStudentsLimit() > 0) {
            event.setStudentsLimit(eventDTO.getStudentsLimit());
        }
        if (eventDTO.getStartDate() != null) {
            event.setStartDate(eventDTO.getStartDate());
        }
        if (eventDTO.getFinishDate() != null) {
            event.setFinishDate(eventDTO.getFinishDate());
        }

        return event;
    }
}
