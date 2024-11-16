// src/main/java/com/g4appdev/eventmanagement/service/CustomUserDetailsService.java

package com.g4appdev.eventmanagement.service;

import com.g4appdev.eventmanagement.entity.UserEntity;
import com.g4appdev.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        // Fetch the user from the database using email address
        Optional<UserEntity> userEntityOptional = userRepository.findByEmailAddress(emailAddress);

        // If user not found, throw exception
        UserEntity userEntity = userEntityOptional.orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + emailAddress)
        );

        // Convert UserEntity to UserDetails
        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmailAddress(),
                userEntity.getPassword(),
                userEntity.getAuthorities() // This should be a collection of GrantedAuthority for roles
        );
    }
}
