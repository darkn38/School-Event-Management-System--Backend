package com.g4appdev.eventmanagement.service;

import com.g4appdev.eventmanagement.entity.EventEntity;
import com.g4appdev.eventmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }
    // Method to save or update an event
    public EventEntity saveEvent(EventEntity event) {
        return eventRepository.save(event); // Save the updated entity
    }
    // filtering the search results by time, approved events and current events
    public List<EventEntity> findByCriteria(String eventType, String location, LocalDate currentDate) {
        return eventRepository.findAll().stream()
                .filter(event -> (eventType == null || event.getEventType().equalsIgnoreCase(eventType))
                        && (location == null || event.getLocation().equalsIgnoreCase(location))
                        && !event.getDate().isBefore(currentDate) // Exclude past events
                        && "APPROVED".equalsIgnoreCase(event.getApprovalStatus())) // Only include approved events
                .collect(Collectors.toList());
    }
    public Optional<EventEntity> getEventById(int id) {
        return eventRepository.findById(id);
    }

    public EventEntity createEvent(EventEntity event) {
        // Auto-approve events created by admins
        if ("admin".equalsIgnoreCase(event.getCreatedBy())) {
            event.setApprovalStatus("APPROVED");
        } else {
            event.setApprovalStatus("PENDING");
        }
        return eventRepository.save(event);
    }

    public EventEntity updateEvent(int id, EventEntity event) {
        Optional<EventEntity> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            EventEntity existing = existingEvent.get();
            existing.setEventName(event.getEventName());
            existing.setEventType(event.getEventType());
            existing.setDate(event.getDate());
            existing.setTime(event.getTime());
            existing.setLocation(event.getLocation());
            existing.setDescription(event.getDescription());
            return eventRepository.save(existing);
        }
        return null; // Return null if the event does not exist
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

    // New methods for approval functionality
    public boolean updateApprovalStatus(int id, String status) {
        Optional<EventEntity> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            EventEntity event = existingEvent.get();
            event.setApprovalStatus(status);
            eventRepository.save(event);
            return true;
        }
        return false;
    }

    public List<EventEntity> findByApprovalStatus(String approvalStatus) {
        return eventRepository.findByApprovalStatus(approvalStatus);
    }
}
