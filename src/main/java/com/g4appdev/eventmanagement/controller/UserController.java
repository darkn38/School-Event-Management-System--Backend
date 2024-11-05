package com.g4appdev.eventmanagement.controller;

import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
