package com.example.flashcards.user;

import java.util.List;

public interface IUserService {
    void updateEmail(Long userId, String newEmail);
    void updatePassword(Long userId, String newPassword);
    void promoteToTeacher(Long userId);
    List<User> getAllUsers();
    User getUserById(Long userId);
}
