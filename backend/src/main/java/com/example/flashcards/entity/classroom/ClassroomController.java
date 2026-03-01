package com.example.flashcards.entity.classroom;

import com.example.flashcards.common.response.ApiResponse;
import com.example.flashcards.entity.classroom.dto.ClassroomCreateRequest;
import com.example.flashcards.entity.classroom.dto.ClassroomResponse;
import com.example.flashcards.entity.classroom.dto.ClassroomUpdateRequest;
import com.example.flashcards.entity.classroom.dto.ClassroomUserResponse;
import com.example.flashcards.entity.learningmaterial.dto.LearningMaterialCreationRequest;
import com.example.flashcards.entity.learningmaterial.dto.LearningMaterialResponse;
import com.example.flashcards.entity.quiz.dto.QuizSeachResponse;
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
                .map(c -> mapToClassroomResponse(c, userId))
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

        return ResponseEntity.ok(ApiResponse.success(mapToClassroomResponse(classroom, userId)));
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
                mapToClassroomResponse(created, userId),
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
                mapToClassroomResponse(updated, userId),
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
                mapToClassroomResponse(classroom, userId),
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

    @DeleteMapping("/{classroomId}/users/{userId}")
    public ResponseEntity<ApiResponse<ClassroomResponse>> removeUserFromClassroom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long classroomId,
            @PathVariable Long userId
    ) {
        Long ownerId = userDetails.getUserId();
        Classroom updated = classroomService.removeUserFromClassroom(ownerId, classroomId, userId);

        return ResponseEntity.ok(ApiResponse.success(
                mapToClassroomResponse(updated, ownerId),
                "User removed from classroom successfully."
        ));
    }

    @PostMapping("/{classroomId}/learning-materials")
    public ResponseEntity<ApiResponse<ClassroomResponse>> addLearningMaterial(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long classroomId,
            @Valid @RequestBody LearningMaterialCreationRequest request
    ) {
        Long userId = userDetails.getUserId();
        Classroom updated = classroomService.addLearningMaterial(userId, classroomId, request);

        return ResponseEntity.ok(ApiResponse.success(
                mapToClassroomResponse(updated, userId),
                "Learning material added successfully."
        ));
    }

    @DeleteMapping("/{classroomId}/learning-materials/{learningMaterialId}")
    public ResponseEntity<ApiResponse<ClassroomResponse>> removeLearningMaterial(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long classroomId,
            @PathVariable Long learningMaterialId
    ) {
        Long userId = userDetails.getUserId();
        Classroom updated = classroomService.removeLearningMaterial(userId, classroomId, learningMaterialId);

        return ResponseEntity.ok(ApiResponse.success(
                mapToClassroomResponse(updated, userId),
                "Learning material removed successfully."
        ));
    }

    @PostMapping("/{classroomId}/quizzes/{quizId}")
    public ResponseEntity<ApiResponse<ClassroomResponse>> addQuizToClassroom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long classroomId,
            @PathVariable Long quizId
    ) {
        Long userId = userDetails.getUserId();
        Classroom updated = classroomService.addQuizToClassroom(userId, classroomId, quizId);

        return ResponseEntity.ok(ApiResponse.success(
                mapToClassroomResponse(updated, userId),
                "Quiz added to classroom successfully."
        ));
    }

    @DeleteMapping("/{classroomId}/quizzes/{quizId}")
    public ResponseEntity<ApiResponse<ClassroomResponse>> removeQuizFromClassroom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long classroomId,
            @PathVariable Long quizId
    ) {
        Long userId = userDetails.getUserId();
        Classroom updated = classroomService.removeQuizFromClassroom(userId, classroomId, quizId);

        return ResponseEntity.ok(ApiResponse.success(
                mapToClassroomResponse(updated, userId),
                "Quiz removed from classroom successfully."
        ));
    }

    private ClassroomResponse mapToClassroomResponse(Classroom classroom, Long currentUserId) {
        boolean isOwner = currentUserId != null
                && classroom.getOwner() != null
                && classroom.getOwner().getUserId() == currentUserId;

        List<ClassroomUserResponse> users = classroom.getUsers().stream()
                .map(user -> new ClassroomUserResponse(
                        user.getUserId(),
                        user.getUsername(),
                        user.getRole().name()
                ))
                .toList();

        List<QuizSeachResponse> quizzes = classroom.getQuizzes().stream()
                .map(quiz -> new QuizSeachResponse(
                        quiz.getQuizId(),
                        quiz.getTitle(),
                        quiz.getDescription(),
                        quiz.getCreator().getUsername(),
                        quiz.getCreator().getRole().name(),
                        quiz.getSubject().getName(),
                        quiz.getFlashcards().size()
                ))
                .toList();

        List<LearningMaterialResponse> materials = classroom.getLearningMaterials().stream()
                .map(material -> new LearningMaterialResponse(
                        material.getLearningMaterialId(),
                        material.getTitle(),
                        material.getContent(),
                        material.getCreator().getUsername()
                ))
                .toList();

        return new ClassroomResponse(
                classroom.getClassroomId(),
                classroom.getTitle(),
                classroom.getDescription(),
                classroom.getNote(),
                classroom.getJoinCode(),
                classroom.getSubject().getName(),
                classroom.getOwner().getUsername(),
                isOwner,
                classroom.getUsers().size(),
                users,
                quizzes,
                materials
        );
    }
}