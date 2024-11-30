package com.g4appdev.eventmanagement.service;

import com.g4appdev.eventmanagement.entity.EventEntity;
import com.g4appdev.eventmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    // Updated to return Optional<EventEntity>
    public Optional<EventEntity> getEventById(int id) {
        return eventRepository.findById(id);
    }

    public EventEntity createEvent(EventEntity event) {
        return eventRepository.save(event);
    }

    public EventEntity updateEvent(int id, EventEntity event) {
        // Ensure the ID is set correctly for updates if needed
        return eventRepository.save(event);
    }

    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }
    
    public List<EventEntity> findByEventTypeAndLocation(String eventType, String location) {
        return eventRepository.findByEventTypeAndLocation(eventType, location);
    }
    
    public List<EventEntity> findUpcomingEvents(LocalDate currentDate) {
        return eventRepository.findUpcomingEvents(currentDate);
    }
    
}
