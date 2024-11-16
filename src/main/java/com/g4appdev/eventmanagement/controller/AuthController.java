package com.g4appdev.eventmanagement.controller;

import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.service.UserService;
import com.g4appdev.eventmanagement.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private JwtUtil jwtUtil;

    // Login endpoint (email & password)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserEntity userEntity) {
        System.out.println("Attempting login for email: " + userEntity.getEmailAddress());

        // Attempt to authenticate the user
        Optional<UserEntity> user = userService.findByEmailAndPassword(userEntity.getEmailAddress(), userEntity.getPassword());

        if (user.isPresent()) {
            System.out.println("User found: " + user.get().getEmailAddress());

            // Load user details and generate JWT token
            UserDetails userDetails = userService.loadUserByUsername(userEntity.getEmailAddress());
            String token = jwtUtil.generateToken(userDetails);
            System.out.println("Generated Token in AuthController: " + token);

            if (token == null || token.isEmpty()) {
                System.out.println("Token generation failed. Please check JwtUtil.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Token generation failed"));
            }

            // Prepare response with role, token, and isAdmin flag
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            String role = user.get().getRole();
            System.out.println("User Role: " + role);
            response.put("role", role);

            // Check if user is an admin
            response.put("isAdmin", "Admin".equalsIgnoreCase(role) ? "true" : "false");

            return ResponseEntity.ok(response);
        } else {
            System.out.println("Authentication failed: Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }
}
