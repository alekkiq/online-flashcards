package com.example.flashcards.auth;

import com.example.flashcards.auth.dto.AuthResponse;
import com.example.flashcards.auth.dto.LoginRequest;
import com.example.flashcards.auth.dto.UserRegistrationRequest;
import com.example.flashcards.common.exceptions.DuplicateResourceException;
import com.example.flashcards.common.exceptions.ResourceNotFoundException;
import com.example.flashcards.common.exceptions.UnauthorizedException;
import com.example.flashcards.user.User;
import com.example.flashcards.user.UserRepository;
import com.example.flashcards.user.UserRole;
import com.example.flashcards.user.exceptions.DuplicateUserException;
import com.example.flashcards.user.exceptions.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(UserRegistrationRequest request) {
        if (this.userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("User", "This username is already taken. Please choose another one.");
        }

        if (this.userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User", "An account with this email already exists.");
        }

        String hashedPassword = this.passwordEncoder.encode(request.password());

        User user = new User(
            request.username(),
            request.email(),
            hashedPassword,
            UserRole.STUDENT
        );

        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public User login(LoginRequest request) {
        User user = this.userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!this.passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        return user;
    }
}
