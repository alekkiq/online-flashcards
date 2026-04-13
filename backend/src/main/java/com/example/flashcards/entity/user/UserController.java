package com.example.flashcards.entity.user;

import com.example.flashcards.common.response.ApiResponse;
import com.example.flashcards.security.CustomUserDetails;
import com.example.flashcards.entity.user.dto.EmailUpdateRequest;
import com.example.flashcards.entity.user.dto.PasswordUpdateRequest;
import com.example.flashcards.entity.user.dto.RoleUpdateRequest;
import com.example.flashcards.entity.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final MessageSource messageSource;

    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    /**
     * Get all users.
     * @return the list of all user responses
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<User> users = this.userService.getAllUsers();
        List<UserResponse> responses = users.stream()
            .map(user -> new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
            ))
            .toList();

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get the current authenticated user's details.
     * @return the current user response
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = this.userService.getUserById(userDetails.getUserId());

        UserResponse response = new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Update the email of the current user.
     * @param request the email update request containing the new email
     * @return the updated user response
     */
    @PutMapping("/me/email")
    public ResponseEntity<ApiResponse<UserResponse>> updateEmail(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody @Valid EmailUpdateRequest request,
        Locale locale
    ) {
        this.userService.updateEmail(userDetails.getUserId(), request.email());
        User user = this.userService.getUserById(userDetails.getUserId());

        UserResponse response = new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );

        return ResponseEntity.ok(
            ApiResponse.success(response, messageSource.getMessage("success.email.updated", null, locale))
        );
    }

    /**
     * Update the password of the current user.
     * @param request the password update request containing the new password
     * @return the updated user response
     */
    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody @Valid PasswordUpdateRequest request,
        Locale locale
    ) {
        this.userService.updatePassword(
            userDetails.getUserId(),
            request.oldPassword(),
            request.newPassword()
        );

        return ResponseEntity.ok(
            ApiResponse.success(null, messageSource.getMessage("success.password.updated", null, locale))
        );
    }

    /**
     * Promote a user to a new role.
     * @param userId the ID of the user to promote
     * @return the updated user response
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
        @PathVariable Long userId,
        @RequestBody @Valid RoleUpdateRequest request,
        Locale locale
    ) {
        this.userService.updateUserRole(userId, request.role());
        User user = this.userService.getUserById(userId);

        UserResponse response = new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );

        return ResponseEntity.ok(
            ApiResponse.success(response, messageSource.getMessage("success.role.updated", null, locale))
        );
    }

    /**
     * Delete a user by ID.
     * @param userId
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId, Locale locale) {
        this.userService.deleteUser(userId);

        return ResponseEntity.ok(
            ApiResponse.success(null, messageSource.getMessage("success.user.deleted", null, locale))
        );
    }
}
