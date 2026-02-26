package com.example.flashcards.entity.classroom;

import com.example.flashcards.common.response.ApiResponse;
import com.example.flashcards.entity.classroom.dto.ClassroomCreateRequest;
import com.example.flashcards.entity.classroom.dto.ClassroomResponse;
import com.example.flashcards.entity.classroom.dto.ClassroomUpdateRequest;
import com.example.flashcards.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/classrooms")
public class ClassroomController {

    private final IClassroomService classroomService;

    public ClassroomController(IClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ClassroomResponse>>> getMyClassrooms(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        List<ClassroomResponse> response = classroomService.getMyClassrooms(userId).stream()
                .map(c -> ClassroomResponse.from(c, userId))
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{classroomId}")
    public ResponseEntity<ApiResponse<ClassroomResponse>> getClassroomById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long classroomId
    ) {
        Long userId = userDetails.getUserId();
        Classroom classroom = classroomService.getClassroomById(classroomId);

        return ResponseEntity.ok(ApiResponse.success(ClassroomResponse.from(classroom, userId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<ClassroomResponse>> createClassroom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ClassroomCreateRequest request
    ) {
        Long userId = userDetails.getUserId();
        Classroom created = classroomService.createClassroom(userId, request);

        return ResponseEntity.ok(ApiResponse.success(
                ClassroomResponse.from(created, userId),
                "Classroom created successfully."
        ));
    }

    @PutMapping("/{classroomId}")
    public ResponseEntity<ApiResponse<ClassroomResponse>> updateClassroom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long classroomId,
            @Valid @RequestBody ClassroomUpdateRequest request
    ) {
        Long userId = userDetails.getUserId();
        Classroom updated = classroomService.updateClassroom(userId, classroomId, request);

        return ResponseEntity.ok(ApiResponse.success(
                ClassroomResponse.from(updated, userId),
                "Classroom updated successfully."
        ));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<ClassroomResponse>> joinByCode(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String code
    ) {
        Long userId = userDetails.getUserId();
        Classroom classroom = classroomService.joinByCode(userId, code);

        return ResponseEntity.ok(ApiResponse.success(
                ClassroomResponse.from(classroom, userId),
                "Joined classroom successfully."
        ));
    }

    @PostMapping("/{classroomId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveClassroom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long classroomId
    ) {
        Long userId = userDetails.getUserId();
        classroomService.leaveClassroom(userId, classroomId);

        return ResponseEntity.ok(ApiResponse.success(null, "Left classroom successfully."));
    }
}