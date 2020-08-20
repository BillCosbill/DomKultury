package com.example.demo.services.interfaces;

import com.example.demo.dto.EventDTO;
import com.example.demo.models.Event;

import java.util.List;

public interface EventService {
    List<Event> findAll();

    Event findById(Long id);

    void deleteEvent(Long id);

    Event updateEvent(EventDTO eventDTO, Long id);

    Event save(Event event);
}
