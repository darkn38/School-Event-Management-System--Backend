package com.g4appdev.eventmanagement.dto;

import java.util.Date;

public class EventRegistrationDTO {
	private int registrationID;
    private String firstName;   // User First Name
    private String lastName;    // User Last Name
    private String emailAddress; // User Email
    private String ticketType;   // Ticket Type
    private String paymentStatus; // Payment Status
    private int eventId;         // Event ID
    private String eventName;    // Event Name
    private String eventType;    // Event Type
    private Date registrationDate; // Registration Date

    // Default Constructor
    public EventRegistrationDTO() {
    }

    // Constructor for Mapping
    public EventRegistrationDTO(int registrationID,String firstName, String lastName, String emailAddress, String ticketType, String paymentStatus, int eventId, String eventName, String eventType, Date registrationDate) {
    	this.registrationID = registrationID;
    	this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.ticketType = ticketType;
        this.paymentStatus = paymentStatus;
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public int getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(int registrationID) {
        this.registrationID = registrationID;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
