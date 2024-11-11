package com.g4appdev.eventmanagement.repository;

import com.g4appdev.eventmanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    
    boolean existsByEmailAddress(String emailAddress);
    
    Optional<UserEntity> findByEmailAddress(String emailAddress);
    
    // Custom query to find user by email and password
    Optional<UserEntity> findByEmailAddressAndPassword(String emailAddress, String password);
}
