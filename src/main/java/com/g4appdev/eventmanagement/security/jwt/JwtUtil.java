package com.g4appdev.eventmanagement.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyBase64; // Inject the Base64-encoded secret key

    private final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24;     // 24 hours
    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60;           // 1 hour

    // Validate the secret key length after injection
    @PostConstruct
    public void init() {
        System.out.println("Loaded JWT Secret Key: " + secretKeyBase64);
        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key is not set. Please check your configuration.");
        }
    }

    // Decode the Base64 secret key for use with HS512
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(secretKeyBase64));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired: " + e.getMessage());
            throw e;
        } catch (JwtException e) {
            System.out.println("Error parsing JWT: " + e.getMessage());
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, int userID) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("authorities", userDetails.getAuthorities());
            claims.put("userID", userID); // Add userID as a claim
            String token = createToken(claims, userDetails.getUsername(), ACCESS_TOKEN_VALIDITY);
            System.out.println("Generated JWT Token: " + token); // Log the generated token
            return token;
        } catch (Exception e) {
            System.out.println("Exception occurred during token generation: " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace
            return null;
        }
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername(), REFRESH_TOKEN_VALIDITY);
    }

    private String createToken(Map<String, Object> claims, String subject, long validity) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + validity))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            System.out.println("Error creating JWT token: " + e.getMessage());
            throw new RuntimeException("Failed to create JWT token", e);
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            System.out.println("Extracted username from token: " + username);
            boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            if (isValid) {
                System.out.println("Token is valid for user: " + username);
            } else {
                System.out.println("Token is invalid or expired for user: " + username);
            }
            return isValid;
        } catch (ExpiredJwtException e) {
            System.out.println("Token validation failed - Token has expired: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            System.out.println("Token is valid.");
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token validation error - Token has expired: " + e.getMessage());
            return false;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            System.out.println("Token validation error: " + e.getMessage());
            return false;
        }
    }
}
