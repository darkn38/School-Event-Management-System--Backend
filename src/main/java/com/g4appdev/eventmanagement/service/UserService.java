package com.g4appdev.eventmanagement.service;

import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by userID
    public Optional<UserEntity> getUserById(int userID) {
        return userRepository.findById(userID);
    }

    // Add a new user
    public UserEntity addUser(UserEntity userEntity) {
        // Here, you might want to validate the userEntity (e.g., check for existing email)
        if (userRepository.existsByEmailAddress(userEntity.getEmailAddress())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        return userRepository.save(userEntity);
    }

    // Update an existing user
    public UserEntity updateUser(int userID, UserEntity userEntity) {
        Optional<UserEntity> existingUser = userRepository.findById(userID);
        if (existingUser.isPresent()) {
            UserEntity userToUpdate = existingUser.get();

            // Update only fields that are provided
            if (userEntity.getFirstName() != null) {
                userToUpdate.setFirstName(userEntity.getFirstName());
            }
            if (userEntity.getLastName() != null) {
                userToUpdate.setLastName(userEntity.getLastName());
            }
            if (userEntity.getEmailAddress() != null) {
                userToUpdate.setEmailAddress(userEntity.getEmailAddress());
            }
            if (userEntity.getRole() != null) {
                userToUpdate.setRole(userEntity.getRole());
            }
            if (userEntity.getPassword() != null) {
                userToUpdate.setPassword(userEntity.getPassword()); // Consider hashing the password before saving
            }

            return userRepository.save(userToUpdate);
        }
        throw new NoSuchElementException("User not found with ID: " + userID);
    }

    // Delete a user
    public boolean deleteUser(int userID) {
        if (userRepository.existsById(userID)) {
            userRepository.deleteById(userID);
            return true; // Deletion was successful
        }
        return false; // User not found for deletion
    }
}
