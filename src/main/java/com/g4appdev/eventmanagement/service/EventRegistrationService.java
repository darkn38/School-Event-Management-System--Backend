package com.g4appdev.eventmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	

    // Create and update Event Registration
    public EventRegistrationEntity saveEventRegistration(EventRegistrationEntity registration) {
        // Fetch and set the UserEntity
        if (registration.getUser() != null && registration.getUser().getUserID() > 0) {
            UserEntity user = userRepository.findById(registration.getUser().getUserID())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + registration.getUser().getUserID()));
            registration.setUser(user);
        } else {
            throw new IllegalArgumentException("User ID is required for event registration.");
        }

        // Fetch and set the EventEntity
        if (registration.getEvent() != null && registration.getEvent().getevent_id() > 0) {
            EventEntity event = eventRepository.findById(registration.getEvent().getevent_id())
                    .orElseThrow(() -> new RuntimeException("Event not found with ID: " + registration.getEvent().getevent_id()));
            registration.setEvent(event);
        } else {
            throw new IllegalArgumentException("Event ID is required for event registration.");
        }

        // Save the registration
        return eventRegistrationRepository.save(registration);
    }

    // Get an EventRegistration by registrationID
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
