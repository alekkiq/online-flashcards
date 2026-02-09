package com.example.flashcards.entity.user;

import com.example.flashcards.config.SecurityConfig;
import com.example.flashcards.config.TestSecurityConfig;
import com.example.flashcards.entity.user.dto.EmailUpdateRequest;
import com.example.flashcards.entity.user.dto.PasswordUpdateRequest;
import com.example.flashcards.entity.user.dto.RoleUpdateRequest;
import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.security.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
            SecurityConfig.class,
            JwtAuthFilter.class,
            JwtService.class,
            CustomUserDetailsService.class,
            CustomAuthenticationEntryPoint.class,
            CustomAccessDeniedHandler.class
        }
    )
)
@Import(TestSecurityConfig.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void initAll() {
        System.out.println("UserController test start");
    }

    @Test
    @DisplayName("getAllUsers(): returns list of users")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getAllUsers() throws Exception {
        User user1 = new User("user1", "user1@test.com", "password");
        user1.setUserId(1L);
        user1.setRole(UserRole.STUDENT);

        User user2 = new User("user2", "user2@test.com", "password");
        user2.setUserId(2L);
        user2.setRole(UserRole.TEACHER);

        when(this.userService.getAllUsers()).thenReturn(List.of(user1, user2));

        this.mockMvc.perform(get("/api/v1/users")
                .with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].userId").value(1))
            .andExpect(jsonPath("$.data[0].username").value("user1"))
            .andExpect(jsonPath("$.data[1].userId").value(2))
            .andExpect(jsonPath("$.data[1].username").value("user2"));

        verify(this.userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("getAllUsers(): forbidden without ADMIN role")
    void getAllUsers_withoutAdminRole_forbidden() throws Exception {
        this.mockMvc.perform(get("/api/v1/users")).andExpect(status().isForbidden());

        verify(this.userService, never()).getAllUsers();
    }

    @Test
    @DisplayName("getCurrentUser(): returns current authenticated user")
    void getCurrentUser() throws Exception {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(5L);
        user.setRole(UserRole.STUDENT);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        when(this.userService.getUserById(5L)).thenReturn(user);

        this.mockMvc.perform(get("/api/v1/users/me")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.userId").value(5))
            .andExpect(jsonPath("$.data.username").value("testuser"))
            .andExpect(jsonPath("$.data.email").value("test@test.com"));

        verify(this.userService, times(1)).getUserById(5L);
    }

    @Test
    @DisplayName("getCurrentUser(): unauthorized without authentication")
    void getCurrentUser_withoutAuth_unauthorized() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/me")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("getCurrentUser(): user not found throws exception")
    void getCurrentUser_userNotFound_throwsException() throws Exception {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(999L);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        when(this.userService.getUserById(999L))
            .thenThrow(new ResourceNotFoundException("User", "User not found"));

        this.mockMvc.perform(get("/api/v1/users/me")
                .with(user(userDetails)))
            .andExpect(status().isNotFound());

        verify(this.userService, times(1)).getUserById(999L);
    }


    @Test
    @DisplayName("updateEmail(): successfully updates email")
    void updateEmail() throws Exception {
        User user = new User("testuser", "newemail@test.com", "password");
        user.setUserId(3L);
        user.setRole(UserRole.STUDENT);

        CustomUserDetails userDetails = new CustomUserDetails(user);
        EmailUpdateRequest request = new EmailUpdateRequest("newemail@test.com");

        doNothing().when(this.userService).updateEmail(3L, "newemail@test.com");
        when(this.userService.getUserById(3L)).thenReturn(user);

        this.mockMvc.perform(put("/api/v1/users/me/email")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Email updated successfully."))
            .andExpect(jsonPath("$.data.email").value("newemail@test.com"));

        verify(this.userService, times(1)).updateEmail(3L, "newemail@test.com");
    }

    @Test
    @DisplayName("updateEmail(): fails when email already exists")
    void updateEmail_duplicateEmail_throwsException() throws Exception {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(3L);

        CustomUserDetails userDetails = new CustomUserDetails(user);
        EmailUpdateRequest request = new EmailUpdateRequest("taken@test.com");

        doThrow(new DuplicateResourceException("User", "Email already in use"))
            .when(this.userService).updateEmail(3L, "taken@test.com");

        this.mockMvc.perform(put("/api/v1/users/me/email")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());

        verify(this.userService, times(1)).updateEmail(3L, "taken@test.com");
    }

    @Test
    @DisplayName("updateEmail(): fails with invalid email format")
    void updateEmail_invalidEmailFormat_badRequest() throws Exception {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(3L);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        EmailUpdateRequest request = new EmailUpdateRequest("invalid-email");

        this.mockMvc.perform(put("/api/v1/users/me/email")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(this.userService, never()).updateEmail(anyLong(), anyString());
    }


    @Test
    @DisplayName("updatePassword(): successfully updates password")
    void updatePassword() throws Exception {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(4L);

        CustomUserDetails userDetails = new CustomUserDetails(user);
        PasswordUpdateRequest request = new PasswordUpdateRequest("oldPassword", "newPassword");

        doNothing().when(this.userService).updatePassword(4L, "oldPassword", "newPassword");

        this.mockMvc.perform(put("/api/v1/users/me/password")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Password updated successfully."));

        verify(this.userService, times(1)).updatePassword(4L, "oldPassword", "newPassword");
    }

    @Test
    @DisplayName("updatePassword(): fails with incorrect old password")
    void updatePassword_incorrectOldPassword_throwsException() throws Exception {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(4L);

        CustomUserDetails userDetails = new CustomUserDetails(user);
        PasswordUpdateRequest request = new PasswordUpdateRequest("wrongPassword", "newPassword");

        doThrow(new IllegalArgumentException("Incorrect old password"))
            .when(this.userService).updatePassword(4L, "wrongPassword", "newPassword");

        this.mockMvc.perform(put("/api/v1/users/me/password")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(this.userService, times(1)).updatePassword(4L, "wrongPassword", "newPassword");
    }

    @Test
    @DisplayName("updatePassword(): fails with blank new password")
    void updatePassword_blankPassword_badRequest() throws Exception {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(4L);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        PasswordUpdateRequest request = new PasswordUpdateRequest("oldPassword", "");

        this.mockMvc.perform(put("/api/v1/users/me/password")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(this.userService, never()).updatePassword(anyLong(), anyString(), anyString());
    }


    @Test
    @DisplayName("updateUserRole(): successfully updates user role")
    void updateUserRole() throws Exception {
        User user = new User("student", "student@test.com", "password");
        user.setUserId(7L);
        user.setRole(UserRole.TEACHER);

        RoleUpdateRequest request = new RoleUpdateRequest(UserRole.TEACHER);

        doNothing().when(this.userService).updateUserRole(7L, UserRole.TEACHER);
        when(this.userService.getUserById(7L)).thenReturn(user);

        this.mockMvc.perform(put("/api/v1/users/7/role")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("User role updated successfully."))
            .andExpect(jsonPath("$.data.role").value("TEACHER"));

        verify(this.userService, times(1)).updateUserRole(7L, UserRole.TEACHER);
    }

    @Test
    @DisplayName("updateUserRole(): forbidden without ADMIN role")
    void updateUserRole_withoutAdminRole_forbidden() throws Exception {
        RoleUpdateRequest request = new RoleUpdateRequest(UserRole.TEACHER);

        this.mockMvc.perform(put("/api/v1/users/7/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());

        verify(this.userService, never()).updateUserRole(any(), any());
    }

    @Test
    @DisplayName("updateUserRole(): user not found throws exception")
    void updateUserRole_userNotFound_throwsException() throws Exception {
        RoleUpdateRequest request = new RoleUpdateRequest(UserRole.TEACHER);

        doThrow(new ResourceNotFoundException("User", "User not found"))
            .when(this.userService).updateUserRole(99L, UserRole.TEACHER);

        this.mockMvc.perform(put("/api/v1/users/99/role")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());

        verify(this.userService, times(1)).updateUserRole(99L, UserRole.TEACHER);
    }

    @Test
    @DisplayName("updateUserRole(): fails with null role")
    void updateUserRole_nullRole_badRequest() throws Exception {
        this.mockMvc.perform(put("/api/v1/users/7/role")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());

        verify(this.userService, never()).updateUserRole(anyLong(), any());
    }


    @Test
    @DisplayName("deleteUser(): successfully deletes user")
    void deleteUser() throws Exception {
        doNothing().when(this.userService).deleteUser(10L);

        this.mockMvc.perform(delete("/api/v1/users/10")
                .with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("User deleted successfully."));

        verify(this.userService, times(1)).deleteUser(10L);
    }

    @Test
    @DisplayName("deleteUser(): forbidden without ADMIN role")
    void deleteUser_withoutAdminRole_forbidden() throws Exception {
        this.mockMvc.perform(delete("/api/v1/users/10")).andExpect(status().isForbidden());

        verify(this.userService, never()).deleteUser(any());
    }

    @Test
    @DisplayName("deleteUser(): user not found throws exception")
    void deleteUser_userNotFound_throwsException() throws Exception {
        doThrow(new ResourceNotFoundException("User", "User not found"))
            .when(this.userService).deleteUser(99L);

        this.mockMvc.perform(delete("/api/v1/users/99")
                .with(user("admin").roles("ADMIN")))
            .andExpect(status().isNotFound());

        verify(this.userService, times(1)).deleteUser(99L);
    }
}
