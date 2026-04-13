package com.example.flashcards.entity.subject;

import com.example.flashcards.common.response.ApiResponse;
import com.example.flashcards.entity.subject.dto.SubjectCreationRequest;
import com.example.flashcards.entity.subject.dto.SubjectResponse;
import com.example.flashcards.entity.subject.dto.SubjectUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    /**
     * Get all subjects in the current language.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAllSubjects() {
        List<Subject> subjects = this.subjectService.getAllSubjects();
        List<SubjectResponse> response = subjects.stream()
            .map(SubjectResponse::from)
            .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get a subject by its ID.
     */
    @GetMapping("/{subjectId}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectById(@PathVariable Long subjectId) {
        Subject subject = this.subjectService.getSubjectById(subjectId);
        return ResponseEntity.ok(ApiResponse.success(SubjectResponse.from(subject)));
    }

    /**
     * Get a subject by its stable code.
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectByCode(@PathVariable String code) {
        Subject subject = this.subjectService.getSubjectByCode(code);
        return ResponseEntity.ok(ApiResponse.success(SubjectResponse.from(subject)));
    }

    /**
     * Create a new subject. (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(
        @Valid @RequestBody SubjectCreationRequest request
    ) {
        Subject subject = new Subject(request.code(), request.name(), request.language());
        Subject createdSubject = this.subjectService.createSubject(subject);

        return ResponseEntity.ok(
            ApiResponse.success(SubjectResponse.from(createdSubject), "Subject created successfully.")
        );
    }

    /**
     * Update an existing subject. (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{subjectId}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(
        @PathVariable Long subjectId,
        @Valid @RequestBody SubjectUpdateRequest request
    ) {
        Subject subject = new Subject(request.code(), request.name(), request.language());
        Subject updatedSubject = this.subjectService.updateSubject(subjectId, subject);

        return ResponseEntity.ok(
            ApiResponse.success(SubjectResponse.from(updatedSubject), "Subject updated successfully.")
        );
    }

    /**
     * Delete a subject by its ID. (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long subjectId) {
        this.subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(ApiResponse.success(null, "Subject deleted successfully."));
    }
}
