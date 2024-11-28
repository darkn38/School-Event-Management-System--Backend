package com.g4appdev.eventmanagement.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class NotificationDTO {

    private String eventName;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String location;
    private String emailAddress;

    // Constructor
    public NotificationDTO(String eventName, LocalDate eventDate, LocalTime eventTime, String location, String emailAddress) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.location = location;
        this.emailAddress = emailAddress;
    }

    // Getters
    public String getEventName() {
        return eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public String getLocation() {
        return location;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    // Setters
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
