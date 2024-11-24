package com.g4appdev.eventmanagement.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name="tbleventregistration")
public class EventRegistrationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int registrationID;
	
	@ManyToOne 
	@JoinColumn (name="userID",nullable = false)
	private UserEntity user;
	
	@ManyToOne
	@JoinColumn(name="eventid", nullable = false )
	private EventEntity event;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date registrationDate;
	
	@Column(name= "ticketType",nullable = false)
	private String ticketType;
	
	@Column (name= "paymentStatus", nullable= false)
	private String paymentStatus;

	public EventRegistrationEntity() {
		super();
	}

	public int getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(int registrationID) {
		this.registrationID = registrationID;
	}
  
	public UserEntity getUser() {   //need user entity
		return user;
	}

	public void setUser(UserEntity user) {  //need user entity
		this.user = user;
	}
	

	public EventEntity getEvent() {  //need event entity
		
		return event;
	}

	public void setEvent(EventEntity event) { //need event entity
		this.event = event;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
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
	

	
}
