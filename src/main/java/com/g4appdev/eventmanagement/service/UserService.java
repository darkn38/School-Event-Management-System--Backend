package com.g4appdev.eventmanagement.service;

import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder for hashing

    // Get all users
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by userID
    public Optional<UserEntity> getUserById(int userID) {
        return userRepository.findById(userID);
    }

    // Add a new user with hashed password
    public UserEntity addUser(UserEntity userEntity) {
        System.out.println("Attempting to register user: " + userEntity.getEmailAddress());

        if (userRepository.existsByEmailAddress(userEntity.getEmailAddress())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        // Hash the password before saving
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    // Update an existing user with hashed password if updated
    public UserEntity updateUser(int userID, UserEntity userEntity) {
        Optional<UserEntity> existingUser = userRepository.findById(userID);
        if (existingUser.isPresent()) {
            UserEntity userToUpdate = existingUser.get();

            // Update fields that are provided
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
                userToUpdate.setPassword(passwordEncoder.encode(userEntity.getPassword())); // Hash password
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

    // Authenticate user by email and password using password encoder
    public Optional<UserEntity> findByEmailAndPassword(String email, String rawPassword) {
        Optional<UserEntity> userOpt = userRepository.findByEmailAddress(email);

        // Verify the password matches the stored encoded password
        if (userOpt.isPresent() && passwordEncoder.matches(rawPassword, userOpt.get().getPassword())) {
            return userOpt;
        }

        return Optional.empty(); // Return empty if authentication fails
    }

    // Load user by username for Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmailAddress(email);

        if (userEntity.isEmpty()) {
            System.out.println("User not found with email: " + email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        UserEntity user = userEntity.get();
        return User.builder()
                .username(user.getEmailAddress())
                .password(user.getPassword())
                .authorities(user.getRole()) // Assuming roles are simple strings like "ROLE_USER" or "ROLE_ADMIN"
                .build();
    }

    // Reset password with hashing
    public UserEntity resetPassword(String email, String newPassword) {
        Optional<UserEntity> userOptional = userRepository.findByEmailAddress(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword)); // Hash the new password
            return userRepository.save(user);
        } else {
            throw new NoSuchElementException("User not found with email: " + email);
        }
    }

    // Check if a user exists by email
    public Optional<UserEntity> getUserByEmail(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress);
    }
    
    
}
