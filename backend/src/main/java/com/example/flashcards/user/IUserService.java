package com.example.flashcards.user;

import java.util.List;

public interface IUserService {
    void updateEmail(Long userId, String newEmail);
    void updatePassword(Long userId, String oldPassword, String newPassword);
    void updateUserRole(Long userId, UserRole role);
    List<User> getAllUsers();
    User getUserById(Long userId);
    User getUserByUsername(String username);
    void deleteUser(Long userId);
}
