package com.example.flashcards.user;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
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
                .orElseThrow(() -> new ResourceNotFoundException("User", userId, "User with ID " + userId + " not found."));

        Optional<User> existing = this.userRepository.findByEmail(newEmail);
        if (existing.isPresent() && !(existing.get().getUserId() == userId)) {
            throw new DuplicateResourceException("User", "This email address is already in use by another account.");
        }

        user.setEmail(newEmail);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId, "User with ID " + userId + " not found."));

        String hashedPassword = this.passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void promoteToTeacher(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId, "User with ID " + userId + " not found."));
        user.setRole(UserRole.TEACHER);
        this.userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId, "User with ID " + userId + " not found."));
    }
}
