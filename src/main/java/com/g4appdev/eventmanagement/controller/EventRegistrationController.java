package com.g4appdev.eventmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g4appdev.eventmanagement.dto.EventRegistrationDTO;
import com.g4appdev.eventmanagement.entity.EventEntity;
import com.g4appdev.eventmanagement.entity.EventRegistrationEntity;
import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.service.EventRegistrationService;
import com.g4appdev.eventmanagement.service.UserService;
import com.g4appdev.eventmanagement.service.EventService;


@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for this controller
@RequestMapping("/api/eventregistrations")
public class EventRegistrationController {

    @Autowired
    private EventRegistrationService eventRegistrationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    // Create or Update EventRegistration
    @PostMapping
    public ResponseEntity<EventRegistrationEntity> saveEventRegistration(@RequestBody EventRegistrationDTO registrationDTO) {
        try {
            // Save registration using the DTO
            EventRegistrationEntity savedRegistration = eventRegistrationService.saveEventRegistration(registrationDTO);
            return ResponseEntity.ok(savedRegistration);
        } catch (IllegalArgumentException e) {
            // Log the exception message and return a bad request response
            e.printStackTrace(); // For debugging purposes
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            return ResponseEntity.status(500).body(null); // Return internal server error for any other exceptions
        }
    }

    // Get All Event Registrations
    @GetMapping
    public ResponseEntity<List<EventRegistrationEntity>> getAllRegistrations() {
        List<EventRegistrationEntity> registrations = eventRegistrationService.getAllRegistrations();
        return ResponseEntity.ok(registrations);
    }

    // Get EventRegistration by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventRegistrationEntity> getEventRegistrationById(@PathVariable int id) {
        return eventRegistrationService.getEventRegistrationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete EventRegistration by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEventRegistrationById(@PathVariable int id) {
        try {
            eventRegistrationService.deleteEventRegistrationById(id);
            return ResponseEntity.ok("Event registration with ID " + id + " has been deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get Registrations by User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventRegistrationEntity>> getRegistrationsByUser(@PathVariable int userId) {
        Optional<UserEntity> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserEntity user = userOptional.get();
        List<EventRegistrationEntity> registrations = eventRegistrationService.getRegistrationsByUser(user);
        return ResponseEntity.ok(registrations);
    }

    // Get Registrations by Event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventRegistrationEntity>> getRegistrationsByEvent(@PathVariable int eventId) {
        Optional<EventEntity> eventOptional = eventService.getEventById(eventId);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        EventEntity event = eventOptional.get();
        List<EventRegistrationEntity> registrations = eventRegistrationService.getRegistrationsByEvent(event);
        return ResponseEntity.ok(registrations);
    }

    // Get Registrations by User and Event
    @PostMapping("/user-event")
    public ResponseEntity<List<EventRegistrationEntity>> getRegistrationsByUserAndEvent(
            @RequestBody UserEntity user,
            @RequestBody EventEntity event) {
        try {
            List<EventRegistrationEntity> registrations = eventRegistrationService.getRegistrationsByUserAndEvent(user, event);
            return ResponseEntity.ok(registrations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
