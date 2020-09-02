package com.example.demo.services.implementations;

import com.example.demo.dto.EventDTO;
import com.example.demo.exceptions.EventNotFoundException;
import com.example.demo.exceptions.EventStudentsLimitHasBeenReachedException;
import com.example.demo.exceptions.UserAlreadyAddedToEventException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.mappers.EventMapper;
import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.interfaces.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    UserRepository userRepository;
    EventMapper eventMapper;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
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

    @Override
    public void addUserToEvent(EventDTO eventDTO, Long eventId, Long userId) {
        if (!eventDTO.getId().equals(eventId)) {
            throw new EventNotFoundException(eventId);
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        List<User> usersAttended = event.getStudents();

        if (usersAttended.contains(user)) {
            throw new UserAlreadyAddedToEventException(user.getUsername(), eventId);
        }

        if (usersAttended.size() >= event.getStudentsLimit()) {
            throw new EventStudentsLimitHasBeenReachedException(eventId);
        } else {
            usersAttended.add(user);
        }

        eventRepository.save(event);
    }

    @Override
    public void deleteUserFromEvent(EventDTO eventDTO, Long eventId, Long userId) {
        if (!eventDTO.getId().equals(eventId)) {
            throw new EventNotFoundException(eventId);
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        List<User> usersAttended = event.getStudents();

        //TODO nie wiem czy powinno sprawdzać czy użytkownik jest na liście uczestników w danym wydarzeniu
        usersAttended.remove(user);

        eventRepository.save(event);
    }
}
