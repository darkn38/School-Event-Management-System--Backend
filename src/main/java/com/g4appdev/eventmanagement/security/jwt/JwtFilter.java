package com.g4appdev.eventmanagement.security.jwt;

import com.g4appdev.eventmanagement.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestPath = request.getServletPath();

        // Skip JWT validation for login and registration endpoints
        if (requestPath.equals("/api/auth/login") || requestPath.equals("/api/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String emailAddress = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                emailAddress = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                System.out.println("Invalid token or error while extracting username: " + e.getMessage());
            }
        } else {
            System.out.println("Authorization header is missing or does not contain Bearer token");
        }

        if (emailAddress != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(emailAddress);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Token is valid, authentication set for user: " + emailAddress);
            } else {
                System.out.println("Token validation failed for user: " + emailAddress);
            }
        } else {
            if (emailAddress == null) {
                System.out.println("No email address extracted from token.");
            } else {
                System.out.println("Security context already contains authentication.");
            }
        }

        filterChain.doFilter(request, response);
    }
}
