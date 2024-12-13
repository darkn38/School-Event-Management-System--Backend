package com.g4appdev.eventmanagement.controller;

import com.g4appdev.eventmanagement.dto.UserProfileDTO;
import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for this controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by userID
    @GetMapping("/{userID}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable("userID") int userID) {
        Optional<UserEntity> user = userService.getUserById(userID);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get current logged-in user's profile
    @GetMapping("/profile")
    public ResponseEntity<UserEntity> getCurrentUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build(); // Unauthorized if no user is authenticated
        }

        String emailAddress = userDetails.getUsername();
        Optional<UserEntity> user = userService.getUserByEmail(emailAddress);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

 // Update the logged-in user's profile
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(
            @RequestBody @Valid UserProfileDTO updatedUserDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String emailAddress = userDetails.getUsername();
            Optional<UserEntity> existingUserOpt = userService.getUserByEmail(emailAddress);

            if (existingUserOpt.isPresent()) {
                UserEntity existingUser = existingUserOpt.get();
                existingUser.setFirstName(updatedUserDTO.getFirstName());
                existingUser.setLastName(updatedUserDTO.getLastName());
                existingUser.setEmailAddress(updatedUserDTO.getEmailAddress());
                UserEntity savedUser = userService.saveUser(existingUser);
                return ResponseEntity.ok(savedUser);
            }
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the profile");
        }
    }

    // Add a new user
    @PostMapping
    public ResponseEntity<UserEntity> addUser(@RequestBody UserEntity userEntity) {
        UserEntity newUser = userService.addUser(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // Update an existing user
    @PutMapping("/{userID}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable("userID") int userID, @RequestBody UserEntity userEntity) {
        Optional<UserEntity> existingUser = userService.getUserById(userID);
        if (existingUser.isPresent()) {
            userEntity.setUserID(userID);
            UserEntity updatedUser = userService.updateUser(userID, userEntity);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a user
    @DeleteMapping("/{userID}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userID") int userID) {
        if (userService.deleteUser(userID)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Register user and respond accordingly
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserEntity newUser) {
        try {
            // Logic to create a new user
            UserEntity savedUser = userService.addUser(newUser);
            if (savedUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }
}
