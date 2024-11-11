package com.g4appdev.eventmanagement.controller;

import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for frontend
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Login endpoint (email & password)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserEntity userEntity) {
        // Check if user exists with the provided email and password
        Optional<UserEntity> user = userService.findByEmailAndPassword(userEntity.getEmailAddress(), userEntity.getPassword());

        if (user.isPresent()) {
            // User found, prepare response with role and isAdmin flag
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");

            // Get the user's role
            String role = user.get().getRole(); // Assuming getRole() returns the role as String
            System.out.println("User Role: " + role); // Log the role for debugging
            response.put("role", role); // Ensure the correct role is added to the response

            // Add an isAdmin flag to indicate if the user is an admin
            if ("Admin".equals(role)) {
                response.put("isAdmin", "true");
            } else {
                response.put("isAdmin", "false");
            }

            return ResponseEntity.ok(response);
        } else {
            // Invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
