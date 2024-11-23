package com.g4appdev.eventmanagement.repository;


import com.g4appdev.eventmanagement.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer> {

}