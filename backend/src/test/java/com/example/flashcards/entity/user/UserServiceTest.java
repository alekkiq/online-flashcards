package com.example.flashcards.entity.user;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    static void initAll() {
        System.out.println("UserService test start");
    }

    @BeforeEach
    void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userService = new UserService(this.userRepository, this.passwordEncoder);
    }

    @Test
    @DisplayName("updateEmail(): successful email update")
    void updateEmailSuccess() {
        User user = new User("janne", "janne@suomi.fi", "password");
        user.setUserId(1L);
        user.setRole(UserRole.TEACHER);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(this.userRepository.findByEmail("uusijanne@suomi.fi")).thenReturn(Optional.empty());
        when(this.userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        this.userService.updateEmail(1L, "uusijanne@suomi.fi");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(this.userRepository, times(1)).save(captor.capture());
        assertEquals("uusijanne@suomi.fi", captor.getValue().getEmail());
    }

    @Test
    @DisplayName("updateEmail(): email already in use throws exception")
    void updateEmail_emailInUse_throwsException() {
        User user = new User("jaana", "jaana@esimerkki.fi", "salasana");
        user.setUserId(2L);
        user.setRole(UserRole.STUDENT);

        User taken = new User("taken", "taken@esimerkki.fi", "password");
        taken.setUserId(3L);

        when(this.userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(this.userRepository.findByEmail("taken@esimerkki.fi")).thenReturn(Optional.of(taken));

        assertThrows(DuplicateResourceException.class, () -> this.userService.updateEmail(2L, "taken@esimerkki.fi"));
        verify(this.userRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateEmail(): user not found throws exception")
    void updateEmail_userNotFound_throwsException() {
        when(this.userRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.userService.updateEmail(42L, "x@y.com"));
    }

    @Test
    @DisplayName("updateEmail(): new email same as old email allowed")
    void updateEmail_newEmailSameAsOldAllowed() {
        User user = new User("user", "email@email.com", "password");
        user.setUserId(5L);

        when(this.userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(this.userRepository.findByEmail("email@email.com")).thenReturn(Optional.of(user));
        when(this.userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        assertDoesNotThrow(() -> this.userService.updateEmail(5L, "email@email.com"));
        verify(this.userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("updatePassword(): successful password update")
    void updatePasswordSuccess() {
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword456";
        String hashedOldPassword = this.passwordEncoder.encode(oldPassword);

        User user = new User("user", "user@test.com", hashedOldPassword);
        user.setUserId(3L);

        when(this.userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(this.userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        this.userService.updatePassword(3L, oldPassword, newPassword);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(this.userRepository, times(1)).save(captor.capture());
        String savedHash = captor.getValue().getPassword();
        assertNotEquals(newPassword, savedHash);
        assertTrue(this.passwordEncoder.matches(newPassword, savedHash));
    }

    @Test
    @DisplayName("updatePassword(): incorrect old password throws exception")
    void updatePassword_incorrectOldPassword_throwsException() {
        String correctPassword = "correct123";
        String hashedPassword = this.passwordEncoder.encode(correctPassword);

        User user = new User("user", "user@test.com", hashedPassword);
        user.setUserId(3L);

        when(this.userRepository.findById(3L)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class,
                () -> this.userService.updatePassword(3L, "wrongPassword", "newPassword"));
        verify(this.userRepository, never()).save(any());
    }

    @Test
    @DisplayName("updatePassword(): user not found throws exception")
    void updatePassword_userNotFound_throwsException() {
        when(this.userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.userService.updatePassword(99L, "old", "new"));
    }

    @Test
    @DisplayName("updateUserRole(): successful role update to TEACHER")
    void updateUserRoleSuccess() {
        User user = new User("student", "student@test.com", "password");
        user.setUserId(4L);
        user.setRole(UserRole.STUDENT);

        when(this.userRepository.findById(4L)).thenReturn(Optional.of(user));
        when(this.userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        this.userService.updateUserRole(4L, UserRole.TEACHER);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(this.userRepository, times(1)).save(captor.capture());
        assertEquals(UserRole.TEACHER, captor.getValue().getRole());
    }

    @Test
    @DisplayName("updateUserRole(): successful role update to STUDENT")
    void updateUserRole_toStudent_success() {
        User user = new User("teacher", "teacher@test.com", "password");
        user.setUserId(6L);
        user.setRole(UserRole.TEACHER);

        when(this.userRepository.findById(6L)).thenReturn(Optional.of(user));
        when(this.userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        this.userService.updateUserRole(6L, UserRole.STUDENT);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(this.userRepository, times(1)).save(captor.capture());
        assertEquals(UserRole.STUDENT, captor.getValue().getRole());
    }

    @Test
    @DisplayName("updateUserRole(): user not found throws exception")
    void updateUserRole_userNotFound_throwsException() {
        when(this.userRepository.findById(77L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.userService.updateUserRole(77L, UserRole.TEACHER));
    }

    @Test
    @DisplayName("getAllUsers(): returns repository list")
    void getAllUsersReturnsList() {
        User user1 = new User("user1", "user1@test.com", "password");
        User user2 = new User("user2", "user2@test.com", "password");

        when(this.userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = this.userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }

    @Test
    @DisplayName("getAllUsers(): returns empty list when no users")
    void getAllUsers_emptyList() {
        when(this.userRepository.findAll()).thenReturn(List.of());

        List<User> result = this.userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getUserById(): returns user when found")
    void getUserByIdReturnsUser() {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(5L);

        when(this.userRepository.findById(5L)).thenReturn(Optional.of(user));

        User result = this.userService.getUserById(5L);

        assertEquals(5L, result.getUserId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    @DisplayName("getUserById(): user not found throws exception")
    void getUserById_userNotFound_throwsException() {
        when(this.userRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.userService.getUserById(123L));
    }

    @Test
    @DisplayName("getUserByUsername(): returns user when found")
    void getUserByUsernameReturnsUser() {
        User user = new User("testuser", "test@test.com", "password");

        when(this.userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = this.userService.getUserByUsername("testuser");

        assertEquals("testuser", result.getUsername());
    }

    @Test
    @DisplayName("getUserByUsername(): user not found throws exception")
    void getUserByUsername_userNotFound_throwsException() {
        when(this.userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.userService.getUserByUsername("nonexistent"));
    }

    @Test
    @DisplayName("deleteUser(): successful deletion")
    void deleteUserSuccess() {
        when(this.userRepository.existsById(10L)).thenReturn(true);
        doNothing().when(this.userRepository).deleteById(10L);

        this.userService.deleteUser(10L);

        verify(this.userRepository, times(1)).deleteById(10L);
    }

    @Test
    @DisplayName("deleteUser(): user not found throws exception")
    void deleteUser_userNotFound_throwsException() {
        when(this.userRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> this.userService.deleteUser(99L));
        verify(this.userRepository, never()).deleteById(any());
    }
}
