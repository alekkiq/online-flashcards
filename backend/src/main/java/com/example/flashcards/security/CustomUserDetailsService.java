package com.example.flashcards.security;

import com.example.flashcards.user.User;
import com.example.flashcards.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of UserDetailsService to load user-specific data.
 * This service retrieves user information from the UserRepository.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(user);
    }
}
