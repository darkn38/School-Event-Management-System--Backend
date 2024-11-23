package com.g4appdev.eventmanagement.service;

import com.g4appdev.eventmanagement.entity.EventEntity;
import com.g4appdev.eventmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public EventEntity getEventById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    public EventEntity createEvent(EventEntity event) {
        return eventRepository.save(event);
    }

    public EventEntity updateEvent(int id, EventEntity event) {
        //event.setEventId(id); // Ensure the ID is set correctly for updates
        return eventRepository.save(event);
    }

    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }
}