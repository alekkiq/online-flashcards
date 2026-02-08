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
     * Get all subjects.
     * @return the list of all subjects
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAllSubjects() {
        List<Subject> subjects = this.subjectService.getAllSubjects();
        List<SubjectResponse> response = subjects.stream()
                .map(subject -> SubjectResponse.from(subject.getName()))
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get a subject by its ID.
     * @param subjectId the ID of the subject to retrieve
     * @return the subject with the specified ID
     */
    @GetMapping("/{subjectId}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectById(@PathVariable Long subjectId) {
        Subject subject = this.subjectService.getSubjectById(subjectId);
        SubjectResponse response = SubjectResponse.from(subject.getName());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get a subject by its name.
     * @param name the name of the subject to retrieve
     * @return the subject with the specified name
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectByName(@Valid @PathVariable String name) {
        Subject subject = this.subjectService.getSubjectByName(name);
        SubjectResponse response = SubjectResponse.from(subject.getName());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Create a new subject. (Admin only)
     * @param request the subject creation request containing the subject name
     * @return the created subject
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(@Valid @RequestBody SubjectCreationRequest request) {
        Subject subject = new Subject(request.name());
        Subject createdSubject = this.subjectService.createSubject(subject);
        SubjectResponse response = SubjectResponse.from(createdSubject.getName());

        return ResponseEntity.ok(ApiResponse.success(response, "Subject created successfully."));
    }

    /**
     * Update an existing subject. (Admin only)
     * @param subjectId the ID of the subject to update
     * @param request the subject update request containing the new subject name
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{subjectId}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(
        @PathVariable Long subjectId,
        @Valid @RequestBody SubjectUpdateRequest request
    ) {
        Subject subject = new Subject(request.name());
        Subject updatedSubject = this.subjectService.updateSubject(subjectId, subject);
        SubjectResponse response = SubjectResponse.from(updatedSubject.getName());

        return ResponseEntity.ok(ApiResponse.success(response, "Subject updated successfully."));
    }

    /**
     * Delete a subject by its ID. (Admin only)
     * @param subjectId the ID of the subject to delete
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long subjectId) {
        this.subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(ApiResponse.success(null, "Subject deleted successfully."));
    }
}
