package com.g4appdev.eventmanagement.controller;

import com.g4appdev.eventmanagement.entity.EventEntity;
import com.g4appdev.eventmanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<EventEntity> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> getEventById(@PathVariable int id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventEntity> createEvent(@RequestBody EventEntity event) {
        try {
            String createdBy = getAuthenticatedUserFromToken(); // Get the authenticated user's email
            if (createdBy == null || createdBy.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if the user is not authenticated
            }

            event.setCreatedBy(createdBy); // Set the `created_by` field
            EventEntity createdEvent = eventService.createEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> updateEvent(@PathVariable int id, @RequestBody EventEntity event) {
        EventEntity updatedEvent = eventService.updateEvent(id, event);
        return updatedEvent != null ? ResponseEntity.ok(updatedEvent) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
    //mapping for search function
    @GetMapping("/search")
    public ResponseEntity<?> searchEvents(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String location) {
        LocalDate today = LocalDate.now();
        List<EventEntity> filteredEvents = eventService.findByCriteria(eventType, location, today);

        if (filteredEvents.isEmpty()) {
            // Custom message for no matching events
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                 .body("No events found matching the given criteria.");
        }

        return ResponseEntity.ok(filteredEvents);
    }


    @GetMapping("/upcoming")
    public ResponseEntity<List<EventEntity>> getUpcomingEvents() {
        List<EventEntity> upcomingEvents = eventService.findUpcomingEvents(LocalDate.now());
        if (upcomingEvents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(upcomingEvents);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<EventEntity> approveEvent(@PathVariable int id) {
        try {
            Optional<EventEntity> eventOptional = eventService.getEventById(id);
            if (eventOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            EventEntity event = eventOptional.get();
            event.setApprovalStatus("APPROVED"); // Use the approval_status field
            EventEntity updatedEvent = eventService.saveEvent(event);
            return ResponseEntity.ok(updatedEvent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<EventEntity> rejectEvent(@PathVariable int id) {
        try {
            Optional<EventEntity> eventOptional = eventService.getEventById(id);
            if (eventOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            EventEntity event = eventOptional.get();
            event.setApprovalStatus("REJECTED"); // Use the approval_status field
            EventEntity updatedEvent = eventService.saveEvent(event);
            return ResponseEntity.ok(updatedEvent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // Helper method to get the authenticated user from the JWT token
    private String getAuthenticatedUserFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername(); // Return the username (email)
        } else if (authentication != null && authentication.getPrincipal() instanceof String) {
            return authentication.getPrincipal().toString(); // Return the principal if it's a string
        }
        return null; // Return null if no authentication is available
    }
}
