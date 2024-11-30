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

}