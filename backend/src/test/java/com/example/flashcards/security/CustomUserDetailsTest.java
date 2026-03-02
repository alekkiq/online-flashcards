package com.example.flashcards.security;

import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @BeforeAll
    static void initAll() {
        System.out.println("CustomUserDetails test start");
    }

    @Test
    @DisplayName("getAuthorities(): returns ROLE_STUDENT for student user")
    void getAuthorities_studentRole() {
        User user = new User("student", "student@test.com", "password");
        user.setRole(UserRole.STUDENT);
        CustomUserDetails details = new CustomUserDetails(user);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ROLE_STUDENT", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("getAuthorities(): returns ROLE_ADMIN for admin user")
    void getAuthorities_adminRole() {
        User user = new User("admin", "admin@test.com", "password");
        user.setRole(UserRole.ADMIN);
        CustomUserDetails details = new CustomUserDetails(user);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("getAuthorities(): returns ROLE_TEACHER for teacher user")
    void getAuthorities_teacherRole() {
        User user = new User("teacher", "teacher@test.com", "password");
        user.setRole(UserRole.TEACHER);
        CustomUserDetails details = new CustomUserDetails(user);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ROLE_TEACHER", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("getUsername(): delegates to User")
    void getUsername_delegatesToUser() {
        User user = new User("myuser", "my@test.com", "password");
        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("myuser", details.getUsername());
    }

    @Test
    @DisplayName("getPassword(): delegates to User")
    void getPassword_delegatesToUser() {
        User user = new User("myuser", "my@test.com", "secret123");
        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("secret123", details.getPassword());
    }

    @Test
    @DisplayName("getUserId(): delegates to User")
    void getUserId_delegatesToUser() {
        User user = new User("myuser", "my@test.com", "password");
        user.setUserId(77L);
        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals(77L, details.getUserId());
    }

    @Test
    @DisplayName("account status methods return true")
    void accountStatusMethods_returnTrue() {
        User user = new User("myuser", "my@test.com", "password");
        CustomUserDetails details = new CustomUserDetails(user);

        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }
}

