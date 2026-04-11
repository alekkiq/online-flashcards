package com.example.flashcards.entity.user;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private static final String ENTITY_NAME = "User";
    private static final String USER_WITH_ID = "User with ID ";
    private static final String NOT_FOUND_SUFFIX = " not found.";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void updateEmail(Long userId, String newEmail) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ENTITY_NAME,
                        userId,
                        USER_WITH_ID + userId + NOT_FOUND_SUFFIX
                ));

        Optional<User> existing = this.userRepository.findByEmail(newEmail);
        if (existing.isPresent() && existing.get().getUserId() != userId) {
            throw new DuplicateResourceException("User", "This email address is already in use by another account.");
        }

        user.setEmail(newEmail);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, UserRole newRole) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ENTITY_NAME,
                        userId,
                        USER_WITH_ID + userId + NOT_FOUND_SUFFIX
                ));
        user.setRole(newRole);
        this.userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ENTITY_NAME,
                        userId,
                        USER_WITH_ID + userId + NOT_FOUND_SUFFIX
                ));
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ENTITY_NAME,
                        "User with username " + username + NOT_FOUND_SUFFIX
                ));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!this.userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    ENTITY_NAME,
                    userId,
                    USER_WITH_ID + userId + NOT_FOUND_SUFFIX
            );
        }
        this.userRepository.deleteById(userId);
    }
}