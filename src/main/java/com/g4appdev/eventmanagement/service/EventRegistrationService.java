package com.g4appdev.eventmanagement.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4appdev.eventmanagement.dto.EventRegistrationDTO;
import com.g4appdev.eventmanagement.dto.NotificationDTO;
import com.g4appdev.eventmanagement.entity.EventEntity;
import com.g4appdev.eventmanagement.entity.EventRegistrationEntity;
import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.repository.EventRegistrationRepository;
import com.g4appdev.eventmanagement.repository.EventRepository;
import com.g4appdev.eventmanagement.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class EventRegistrationService {

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;
    

    @Autowired
    private UserService userService; // Inject UserService

    @Autowired
    private EventService eventService; // Inject EventService


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
    
    /**
     * Fetch notifications for events happening within the next 2 days.
     *
     * @return List of NotificationDTO for upcoming events.
     */
    public List<NotificationDTO> getUpcomingEventNotifications(LocalDate startDate, LocalDate endDate) {
        // Fetch all unique events happening within the specified date range
        List<EventEntity> upcomingEvents = eventRepository.findEventsWithinDateRange(startDate, endDate);

        // Map the events to NotificationDTOs
        return upcomingEvents.stream()
                .map(event -> new NotificationDTO(
                        event.getevent_name(),  // Event name
                        event.getDate(),        // Event date
                        event.getTime(),        // Event time
                        event.getLocation(),    // Event location
                        null                    // No specific email, since it's not user-specific
                ))
                .distinct() // Ensure distinct events
                .collect(Collectors.toList());
    }
    
    public boolean isAlreadyRegistered(int userID, int eventID) {
        return eventRegistrationRepository.existsByUser_UserIDAndEvent_EventID(userID, eventID);
    }

    public void registerUserForEvent(int userID, int eventID) {
        UserEntity user = userRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        EventEntity event = eventRepository.findById(eventID)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        EventRegistrationEntity registration = new EventRegistrationEntity();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(new Date());
        registration.setTicketType("Standard"); // Or fetch this dynamically
        registration.setPaymentStatus("Paid"); // Adjust based on your logic

        eventRegistrationRepository.save(registration);
    }
    
    @Transactional
    public EventRegistrationEntity updateEventRegistration(int id, EventRegistrationDTO registrationDTO) {
        // Fetch the existing registration
        EventRegistrationEntity existingRegistration = eventRegistrationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found"));

        // Fetch the user by email address
        UserEntity user = userService.getUserByEmail(registrationDTO.getEmailAddress())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch the event by event ID
        EventEntity event = eventService.getEventById(registrationDTO.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        // Update necessary fields
        existingRegistration.setUser(user); // Link the user
        existingRegistration.setEvent(event); // Link the event
        existingRegistration.setPaymentStatus(registrationDTO.getPaymentStatus());
        existingRegistration.setTicketType(registrationDTO.getTicketType());

        // Save and return the updated registration
        return eventRegistrationRepository.save(existingRegistration);
    }

}
