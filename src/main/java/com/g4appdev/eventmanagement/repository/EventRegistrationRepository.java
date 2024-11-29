package com.g4appdev.eventmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.g4appdev.eventmanagement.entity.EventEntity;
import com.g4appdev.eventmanagement.entity.EventRegistrationEntity;
import com.g4appdev.eventmanagement.entity.UserEntity;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Integer> {

    // Existing methods
    List<EventRegistrationEntity> findByUser(UserEntity user);

    List<EventRegistrationEntity> findByEvent(EventEntity event);

    List<EventRegistrationEntity> findByUserAndEvent(UserEntity user, EventEntity event);

    EventRegistrationEntity findByRegistrationID(int registrationID);

 // Fetch all event registrations within a date range, avoiding duplicates
    @Query("SELECT DISTINCT r FROM EventRegistrationEntity r " +
           "JOIN FETCH r.event e " +
           "JOIN FETCH r.user u " +
           "WHERE e.date BETWEEN :startDate AND :endDate")
    List<EventRegistrationEntity> findUpcomingEvents(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate);
    
 // Derived query using Spring Data JPA
    boolean existsByUser_UserIDAndEvent_EventID(int userID, int eventID);

    // Alternatively, a custom JPQL query
    @Query("SELECT CASE WHEN COUNT(er) > 0 THEN TRUE ELSE FALSE END FROM EventRegistrationEntity er WHERE er.user.userID = :userID AND er.event.eventID = :eventID")
    boolean isUserRegisteredForEvent(@Param("userID") int userID, @Param("eventID") int eventID);

 }
