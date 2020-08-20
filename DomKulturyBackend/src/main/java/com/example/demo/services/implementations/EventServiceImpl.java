package com.example.demo.services.implementations;

import com.example.demo.dto.EventDTO;
import com.example.demo.exceptions.EventNotFoundException;
import com.example.demo.mappers.EventMapper;
import com.example.demo.models.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.services.interfaces.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    EventMapper eventMapper;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    @Override
    public void deleteEvent(Long id) {
        Event event = findById(id);
        eventRepository.delete(event);
    }

    @Override
    public Event updateEvent(EventDTO eventDTO, Long id) {
        //TODO inny wyjątek, żeby się zgadzało
        if (!eventDTO.getId().equals(id)) {
            throw new EventNotFoundException(id);
        }

        Event event = eventMapper.toEvent(eventDTO, id);

        return save(event);
    }

    //TODO metoda save nie jest chyba potrzebna w żadnej implementacji serwisu, usunąć w przyszłości
    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }
}
