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
        // Log or print user details for debugging
        System.out.println("Attempting to register user: " + userEntity.getEmailAddress());

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
 // Method to find user by email and password
    public Optional<UserEntity> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAddressAndPassword(email, password);
    }
    // for reset password
    public UserEntity resetPassword(String email, String newPassword) {
        Optional<UserEntity> userOptional = userRepository.findByEmailAddress(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setPassword(newPassword); // Consider hashing the password
            return userRepository.save(user);
        } else {
            throw new NoSuchElementException("User not found with email: " + email);
        }
    }
    // for responses if the email is already in the database
    public Optional<UserEntity> getUserByEmail(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress);
    }

}
