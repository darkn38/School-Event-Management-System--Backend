package com.g4appdev.eventmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.g4appdev.eventmanagement.entity.EventEntity;
import com.g4appdev.eventmanagement.entity.EventRegistrationEntity;
import com.g4appdev.eventmanagement.entity.UserEntity;

@Repository 
public interface EventRegistrationRepository extends JpaRepository <EventRegistrationEntity, Integer> {
	
	//public EventRegistrationEntity findByRegistration (int registrationID);
	
	List <EventRegistrationEntity> findByUser(UserEntity user);
	
	List<EventRegistrationEntity> findByEvent(EventEntity event);
	
	List <EventRegistrationEntity> findByUserAndEvent(UserEntity user, EventEntity event);
	
	EventRegistrationEntity findByRegistrationID(int registrationID);
	

}
