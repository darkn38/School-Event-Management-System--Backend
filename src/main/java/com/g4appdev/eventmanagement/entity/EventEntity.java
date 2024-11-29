package com.g4appdev.eventmanagement.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "event_entity")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventID;
    
    @Column(name = "event_name")
    private String event_name;
    
    @Column(name = "event_type")
    private String eventType;
    
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "time")
    private LocalTime time;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "description")
    private String description;

    // Constructors

    public EventEntity() {
        // Default constructor
    }

    public EventEntity(String event_name, String event_type, LocalDate date, LocalTime time, String location, String description) {
        this.event_name = event_name;
        this.eventType = event_type;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
    }

    // Getters and Setters

    public int getevent_id() {
        return eventID;
    }

    public void setevent_id(int event_id) {
        this.eventID = event_id;
    }

    public String getevent_name() {
        return event_name;
    }

    public void setevent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getevent_type() {
        return eventType;
    }

    public void setevent_type(String event_type) {
        this.eventType = event_type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}