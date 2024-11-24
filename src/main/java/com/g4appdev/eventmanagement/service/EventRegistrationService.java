package com.g4appdev.eventmanagement.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4appdev.eventmanagement.dto.EventRegistrationDTO;
import com.g4appdev.eventmanagement.entity.EventEntity;
import com.g4appdev.eventmanagement.entity.EventRegistrationEntity;
import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.repository.EventRegistrationRepository;
import com.g4appdev.eventmanagement.repository.EventRepository;
import com.g4appdev.eventmanagement.repository.UserRepository;

@Service
public class EventRegistrationService {

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    // EventRegistrationService - Save or Update Event Registration
    public EventRegistrationEntity saveEventRegistration(EventRegistrationDTO registrationDTO) {
        EventRegistrationEntity registration = new EventRegistrationEntity();
        registration.setTicketType(registrationDTO.getTicketType());
        registration.setPaymentStatus(registrationDTO.getPaymentStatus());
        registration.setRegistrationDate(new Date()); // Set the registration date to the current date

        // Fetch and set the UserEntity by email address
        Optional<UserEntity> userOptional = userRepository.findByEmailAddress(registrationDTO.getEmailAddress());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + registrationDTO.getEmailAddress());
        }
        registration.setUser(userOptional.get());

        // Fetch and set the EventEntity by event ID
        Optional<EventEntity> eventOptional = eventRepository.findById(registrationDTO.getEventId());
        if (eventOptional.isEmpty()) {
            throw new IllegalArgumentException("Event not found with ID: " + registrationDTO.getEventId());
        }
        registration.setEvent(eventOptional.get());

        // Save the registration entity
        return eventRegistrationRepository.save(registration);
    }

    // Get an EventRegistration by registration ID
    public Optional<EventRegistrationEntity> getEventRegistrationById(int registrationID) {
        return eventRegistrationRepository.findById(registrationID);
    }

    // Get all Event Registrations
    public List<EventRegistrationEntity> getAllRegistrations() {
        return eventRegistrationRepository.findAll();
    }

    // Delete an EventRegistration by ID
    public void deleteEventRegistrationById(int id) {
        if (!eventRegistrationRepository.existsById(id)) {
            throw new RuntimeException("Event Registration not found with ID: " + id);
        }
        eventRegistrationRepository.deleteById(id);
    }

    // Get registrations by User
    public List<EventRegistrationEntity> getRegistrationsByUser(UserEntity user) {
        if (!userRepository.existsById(user.getUserID())) {
            throw new RuntimeException("User not found with ID: " + user.getUserID());
        }
        return eventRegistrationRepository.findByUser(user);
    }

    // Get registrations by Event
    public List<EventRegistrationEntity> getRegistrationsByEvent(EventEntity event) {
        if (!eventRepository.existsById(event.getevent_id())) {
            throw new RuntimeException("Event not found with ID: " + event.getevent_id());
        }
        return eventRegistrationRepository.findByEvent(event);
    }

    // Get registrations by User and Event
    public List<EventRegistrationEntity> getRegistrationsByUserAndEvent(UserEntity user, EventEntity event) {
        if (!userRepository.existsById(user.getUserID())) {
            throw new RuntimeException("User not found with ID: " + user.getUserID());
        }
        if (!eventRepository.existsById(event.getevent_id())) {
            throw new RuntimeException("Event not found with ID: " + event.getevent_id());
        }
        return eventRegistrationRepository.findByUserAndEvent(user, event);
    }
}
