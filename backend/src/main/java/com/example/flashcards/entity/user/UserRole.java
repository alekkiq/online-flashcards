package com.example.flashcards.entity.user;

/**
 * Enumeration representing the roles of a user in the application.
 * STUDENT: A common user who can study flashcards.
 * TEACHER: A promoted user who can create and manage their own flashcards.
 * ADMIN: A user with administrative privileges.
 */
public enum UserRole {
    STUDENT,
    TEACHER,
    ADMIN
}
