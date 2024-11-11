package com.g4appdev.eventmanagement.controller;

import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    // Get the currently authenticated user's details
    @GetMapping("/current")
    public ResponseEntity<UserEntity> getCurrentUserDetails(Principal principal) {
        String username = principal.getName();  // Get the username of the currently authenticated user
        Optional<UserEntity> user = userService.getUserByUsername(username); // Fetch user by username
        
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
