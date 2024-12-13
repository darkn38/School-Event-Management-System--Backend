package com.g4appdev.eventmanagement.repository;

import com.g4appdev.eventmanagement.entity.EventEntity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer> {

    @Query("SELECT e FROM EventEntity e WHERE e.date BETWEEN :startDate AND :endDate ORDER BY e.date ASC")
    List<EventEntity> findEventsWithinDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    List<EventEntity> findByEventTypeAndLocation(String eventType, String location);
    
    @Query("SELECT e FROM EventEntity e WHERE e.date >= :currentDate ORDER BY e.date ASC")
    List<EventEntity> findUpcomingEvents(LocalDate currentDate);

    // New Query: Find events by approval status
    List<EventEntity> findByApprovalStatus(String approvalStatus);

    // New Query: Find events by approval status and date range
    @Query("SELECT e FROM EventEntity e WHERE e.approvalStatus = :approvalStatus AND e.date BETWEEN :startDate AND :endDate ORDER BY e.date ASC")
    List<EventEntity> findEventsByApprovalStatusAndDateRange(
            @Param("approvalStatus") String approvalStatus,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    // query for events approval status , existing events
    @Query("SELECT e FROM EventEntity e " +
    	       "WHERE (:eventType IS NULL OR e.eventType = :eventType) " +
    	       "AND (:location IS NULL OR e.location = :location) " +
    	       "AND e.date >= :currentDate " +
    	       "AND e.approvalStatus = 'APPROVED'")
    	List<EventEntity> findEventsByCriteria(
    	        @Param("eventType") String eventType,
    	        @Param("location") String location,
    	        @Param("currentDate") LocalDate currentDate);
}
