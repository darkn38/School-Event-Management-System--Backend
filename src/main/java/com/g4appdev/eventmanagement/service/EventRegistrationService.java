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
    private UserService userService;

    @Autowired
    private EventService eventService;

    public EventRegistrationEntity saveEventRegistration(EventRegistrationDTO registrationDTO) {
        EventRegistrationEntity registration = new EventRegistrationEntity();
        registration.setTicketType(registrationDTO.getTicketType());
        registration.setPaymentStatus(registrationDTO.getPaymentStatus());
        registration.setRegistrationDate(new Date());

        // Fetch and set the UserEntity
        UserEntity user = userRepository.findByEmailAddress(registrationDTO.getEmailAddress())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + registrationDTO.getEmailAddress()));
        registration.setUser(user);

        // Fetch and validate the EventEntity
        EventEntity event = eventRepository.findById(registrationDTO.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + registrationDTO.getEventId()));

        // Ensure the event is approved before allowing registration
        if (!"APPROVED".equals(event.getApprovalStatus())) {
            throw new IllegalArgumentException("Cannot register for unapproved event: " + event.getEventName());
        }
        registration.setEvent(event);

        return eventRegistrationRepository.save(registration);
    }

    public Optional<EventRegistrationEntity> getEventRegistrationById(int registrationID) {
        return eventRegistrationRepository.findById(registrationID);
    }

    public List<EventRegistrationDTO> getAllRegistrations() {
        return eventRegistrationRepository.findAll().stream()
            .map(registration -> new EventRegistrationDTO(
            	registration.getRegistrationID(), 
                registration.getUser().getFirstName(),       // User first name
                registration.getUser().getLastName(),        // User last name
                registration.getUser().getEmailAddress(),    // User email
                registration.getTicketType(),                // Ticket type
                registration.getPaymentStatus(),             // Payment status
                registration.getEvent().getEventID(),        // Event ID
                registration.getEvent().getEventName(),      // Event name
                registration.getEvent().getEventType(),       // Event type
                registration.getRegistrationDate()
            ))
            .collect(Collectors.toList());
    }

    public void deleteEventRegistrationById(int id) {
        if (!eventRegistrationRepository.existsById(id)) {
            throw new RuntimeException("Event Registration not found with ID: " + id);
        }
        eventRegistrationRepository.deleteById(id);
    }

    public List<EventRegistrationEntity> getRegistrationsByUser(UserEntity user) {
        if (!userRepository.existsById(user.getUserID())) {
            throw new RuntimeException("User not found with ID: " + user.getUserID());
        }
        return eventRegistrationRepository.findByUser(user);
    }

    public List<EventRegistrationEntity> getRegistrationsByEvent(EventEntity event) {
        if (!eventRepository.existsById(event.getEventID())) {
            throw new RuntimeException("Event not found with ID: " + event.getEventID());
        }
        return eventRegistrationRepository.findByEvent(event);
    }

    public List<EventRegistrationEntity> getRegistrationsByUserAndEvent(UserEntity user, EventEntity event) {
        if (!userRepository.existsById(user.getUserID())) {
            throw new RuntimeException("User not found with ID: " + user.getUserID());
        }
        if (!eventRepository.existsById(event.getEventID())) {
            throw new RuntimeException("Event not found with ID: " + event.getEventID());
        }
        return eventRegistrationRepository.findByUserAndEvent(user, event);
    }

    public List<NotificationDTO> getUpcomingEventNotifications(LocalDate startDate, LocalDate endDate) {
        List<EventEntity> upcomingEvents = eventRepository.findEventsWithinDateRange(startDate, endDate);

        return upcomingEvents.stream()
                .filter(event -> "APPROVED".equals(event.getApprovalStatus())) // Only notify about approved events
                .map(event -> new NotificationDTO(
                        event.getEventName(),
                        event.getDate(),
                        event.getTime(),
                        event.getLocation(),
                        null
                ))
                .distinct()
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

        if (!"APPROVED".equals(event.getApprovalStatus())) {
            throw new IllegalArgumentException("Cannot register for unapproved event.");
        }

        EventRegistrationEntity registration = new EventRegistrationEntity();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(new Date());
        registration.setTicketType("Standard");
        registration.setPaymentStatus("Paid");

        eventRegistrationRepository.save(registration);
    }

    @Transactional
    public EventRegistrationEntity updateEventRegistration(int id, EventRegistrationDTO registrationDTO) {
        EventRegistrationEntity existingRegistration = eventRegistrationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found"));

        UserEntity user = userService.getUserByEmail(registrationDTO.getEmailAddress())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        EventEntity event = eventService.getEventById(registrationDTO.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (!"APPROVED".equals(event.getApprovalStatus())) {
            throw new IllegalArgumentException("Cannot update registration for unapproved event.");
        }

        existingRegistration.setUser(user);
        existingRegistration.setEvent(event);
        existingRegistration.setPaymentStatus(registrationDTO.getPaymentStatus());
        existingRegistration.setTicketType(registrationDTO.getTicketType());

        return eventRegistrationRepository.save(existingRegistration);
    }
    
    public boolean doesRegistrationExist(int id) {
        return eventRegistrationRepository.existsById(id);
    }

}
