package com.example.flashcards.entity.subject;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.config.SecurityConfig;
import com.example.flashcards.config.TestSecurityConfig;
import com.example.flashcards.entity.subject.dto.SubjectCreationRequest;
import com.example.flashcards.entity.subject.dto.SubjectUpdateRequest;
import com.example.flashcards.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = SubjectController.class,
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
class SubjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubjectService subjectService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void initAll() {
        System.out.println("SubjectController test start");
    }

    @Test
    @DisplayName("getAllSubjects(): returns list of subjects")
    void getAllSubjects() throws Exception {
        Subject subject1 = new Subject("Mathematics");
        subject1.setSubjectId(1L);

        Subject subject2 = new Subject("Physics");
        subject2.setSubjectId(2L);

        when(this.subjectService.getAllSubjects()).thenReturn(List.of(subject1, subject2));

        this.mockMvc.perform(get("/api/v1/subjects")
                .with(user("user").roles("STUDENT")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].name").value("Mathematics"))
            .andExpect(jsonPath("$.data[1].name").value("Physics"));

        verify(this.subjectService, times(1)).getAllSubjects();
    }

    @Test
    @DisplayName("getSubjectById(): returns subject by ID")
    void getSubjectById() throws Exception {
        Subject subject = new Subject("Chemistry");
        subject.setSubjectId(5L);

        when(this.subjectService.getSubjectById(5L)).thenReturn(subject);

        this.mockMvc.perform(get("/api/v1/subjects/5")
                .with(user("user").roles("STUDENT")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.name").value("Chemistry"));

        verify(this.subjectService, times(1)).getSubjectById(5L);
    }

    @Test
    @DisplayName("getSubjectById(): subject not found throws exception")
    void getSubjectById_subjectNotFound_throwsException() throws Exception {
        when(this.subjectService.getSubjectById(99L))
            .thenThrow(new ResourceNotFoundException("Subject", "Subject not found"));

        this.mockMvc.perform(get("/api/v1/subjects/99")
                .with(user("user").roles("STUDENT")))
            .andExpect(status().isNotFound());

        verify(this.subjectService, times(1)).getSubjectById(99L);
    }

    @Test
    @DisplayName("getSubjectByName(): returns subject by name")
    void getSubjectByName() throws Exception {
        Subject subject = new Subject("Biology");
        subject.setSubjectId(3L);

        when(this.subjectService.getSubjectByName("Biology")).thenReturn(subject);

        this.mockMvc.perform(get("/api/v1/subjects/name/Biology")
                .with(user("user").roles("STUDENT")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.name").value("Biology"));

        verify(this.subjectService, times(1)).getSubjectByName("Biology");
    }

    @Test
    @DisplayName("getSubjectByName(): subject not found throws exception")
    void getSubjectByName_subjectNotFound_throwsException() throws Exception {
        when(this.subjectService.getSubjectByName("NonExistent"))
            .thenThrow(new ResourceNotFoundException("Subject", "Subject not found"));

        this.mockMvc.perform(get("/api/v1/subjects/name/NonExistent")
                .with(user("user").roles("STUDENT")))
            .andExpect(status().isNotFound());

        verify(this.subjectService, times(1)).getSubjectByName("NonExistent");
    }

    @Test
    @DisplayName("createSubject(): successfully creates subject")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void createSubject() throws Exception {
        Subject subject = new Subject("History");
        subject.setSubjectId(7L);

        SubjectCreationRequest request = new SubjectCreationRequest("History");

        when(this.subjectService.createSubject(any(Subject.class))).thenReturn(subject);

        this.mockMvc.perform(post("/api/v1/subjects")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Subject created successfully."))
            .andExpect(jsonPath("$.data.name").value("History"));

        verify(this.subjectService, times(1)).createSubject(any(Subject.class));
    }

    @Test
    @DisplayName("createSubject(): forbidden without ADMIN role")
    void createSubject_withoutAdminRole_forbidden() throws Exception {
        SubjectCreationRequest request = new SubjectCreationRequest("History");

        this.mockMvc.perform(post("/api/v1/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());

        verify(this.subjectService, never()).createSubject(any());
    }

    @Test
    @DisplayName("createSubject(): fails with duplicate subject name")
    void createSubject_duplicateName_throwsException() throws Exception {
        SubjectCreationRequest request = new SubjectCreationRequest("Mathematics");

        when(this.subjectService.createSubject(any(Subject.class)))
            .thenThrow(new DuplicateResourceException("Subject", "Subject already exists"));

        this.mockMvc.perform(post("/api/v1/subjects")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());

        verify(this.subjectService, times(1)).createSubject(any(Subject.class));
    }

    @Test
    @DisplayName("createSubject(): fails with blank name")
    void createSubject_blankName_badRequest() throws Exception {
        SubjectCreationRequest request = new SubjectCreationRequest("");

        this.mockMvc.perform(post("/api/v1/subjects")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(this.subjectService, never()).createSubject(any());
    }

    @Test
    @DisplayName("updateSubject(): successfully updates subject")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void updateSubject() throws Exception {
        Subject subject = new Subject("Updated Math");
        subject.setSubjectId(10L);

        SubjectUpdateRequest request = new SubjectUpdateRequest("Updated Math");

        when(this.subjectService.updateSubject(eq(10L), any(Subject.class))).thenReturn(subject);

        this.mockMvc.perform(put("/api/v1/subjects/10")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Subject updated successfully."))
            .andExpect(jsonPath("$.data.name").value("Updated Math"));

        verify(this.subjectService, times(1)).updateSubject(eq(10L), any(Subject.class));
    }

    @Test
    @DisplayName("updateSubject(): forbidden without ADMIN role")
    void updateSubject_withoutAdminRole_forbidden() throws Exception {
        SubjectUpdateRequest request = new SubjectUpdateRequest("Updated Math");

        this.mockMvc.perform(put("/api/v1/subjects/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());

        verify(this.subjectService, never()).updateSubject(anyLong(), any());
    }

    @Test
    @DisplayName("updateSubject(): subject not found throws exception")
    void updateSubject_subjectNotFound_throwsException() throws Exception {
        SubjectUpdateRequest request = new SubjectUpdateRequest("Updated Name");

        when(this.subjectService.updateSubject(eq(99L), any(Subject.class)))
            .thenThrow(new ResourceNotFoundException("Subject", "Subject not found"));

        this.mockMvc.perform(put("/api/v1/subjects/99")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());

        verify(this.subjectService, times(1)).updateSubject(eq(99L), any(Subject.class));
    }

    @Test
    @DisplayName("deleteSubject(): successfully deletes subject")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void deleteSubject() throws Exception {
        doNothing().when(this.subjectService).deleteSubject(15L);

        this.mockMvc.perform(delete("/api/v1/subjects/15")
                .with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Subject deleted successfully."));

        verify(this.subjectService, times(1)).deleteSubject(15L);
    }

    @Test
    @DisplayName("deleteSubject(): forbidden without ADMIN role")
    void deleteSubject_withoutAdminRole_forbidden() throws Exception {
        this.mockMvc.perform(delete("/api/v1/subjects/15"))
            .andExpect(status().isForbidden());

        verify(this.subjectService, never()).deleteSubject(any());
    }

    @Test
    @DisplayName("deleteSubject(): subject not found throws exception")
    void deleteSubject_subjectNotFound_throwsException() throws Exception {
        doThrow(new ResourceNotFoundException("Subject", "Subject not found"))
            .when(this.subjectService).deleteSubject(99L);

        this.mockMvc.perform(delete("/api/v1/subjects/99")
                .with(user("admin").roles("ADMIN")))
            .andExpect(status().isNotFound());

        verify(this.subjectService, times(1)).deleteSubject(99L);
    }
}