package com.example.flashcards.entity.user;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.InvalidRequestException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private static final String ENTITY_NAME = "User";

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
                        "error.user.notFound",
                        new Object[]{userId}
                ));

        Optional<User> existing = this.userRepository.findByEmail(newEmail);
        if (existing.isPresent() && existing.get().getUserId() != userId) {
            throw new DuplicateResourceException("User", "error.user.email.inUse", null);
        }

        user.setEmail(newEmail);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidRequestException("error.user.password.incorrect", null);
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
                        "error.user.notFound",
                        new Object[]{userId}
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
                        "error.user.notFound",
                        new Object[]{userId}
                ));
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ENTITY_NAME,
                        "error.user.notFound",
                        new Object[]{username}
                ));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!this.userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    ENTITY_NAME,
                    userId,
                    "error.user.notFound",
                    new Object[]{userId}
            );
        }
        this.userRepository.deleteById(userId);
    }
}