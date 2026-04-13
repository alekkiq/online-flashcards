package com.example.flashcards.entity.classroom;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.config.SecurityConfig;
import com.example.flashcards.config.TestSecurityConfig;
import com.example.flashcards.entity.classroom.dto.ClassroomCreateRequest;
import com.example.flashcards.entity.classroom.dto.ClassroomUpdateRequest;
import com.example.flashcards.entity.learningmaterial.LearningMaterial;
import com.example.flashcards.entity.learningmaterial.dto.LearningMaterialCreationRequest;
import com.example.flashcards.entity.quiz.Quiz;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRole;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ClassroomController.class,
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
class ClassroomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IClassroomService classroomService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void initAll() {
        System.out.println("ClassroomController test start");
    }

    private User createTeacher() {
        User teacher = new User("teacher", "teacher@test.com", "password");
        teacher.setUserId(1L);
        teacher.setRole(UserRole.TEACHER);
        return teacher;
    }

    private User createStudent() {
        User student = new User("student", "student@test.com", "password");
        student.setUserId(2L);
        student.setRole(UserRole.STUDENT);
        return student;
    }

    private Classroom createTestClassroom(User owner) {
        Subject subject = new Subject("math", "Mathematics", "en");
        subject.setSubjectId(10L);
        Classroom classroom = new Classroom("Test Class", "Description", "Note", "ABC123", "en", owner, subject);
        classroom.setClassroomId(1L);
        classroom.addUser(owner);
        return classroom;
    }

    @Test
    @DisplayName("getMyClassrooms(): returns list of classrooms")
    void getMyClassrooms_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);
        Classroom classroom = createTestClassroom(teacher);

        when(this.classroomService.getMyClassrooms(1L)).thenReturn(List.of(classroom));

        this.mockMvc.perform(get("/api/v1/classrooms/me")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data[0].title").value("Test Class"));

        verify(this.classroomService, times(1)).getMyClassrooms(1L);
    }

    @Test
    @DisplayName("getMyClassrooms(): unauthenticated returns forbidden")
    void getMyClassrooms_unauthenticated_forbidden() throws Exception {
        this.mockMvc.perform(get("/api/v1/classrooms/me"))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("getClassroomById(): returns classroom")
    void getClassroomById_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);
        Classroom classroom = createTestClassroom(teacher);

        when(this.classroomService.getClassroomById(1L, 1L)).thenReturn(classroom);

        this.mockMvc.perform(get("/api/v1/classrooms/1")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.title").value("Test Class"))
            .andExpect(jsonPath("$.data.isOwner").value(true));

        verify(this.classroomService, times(1)).getClassroomById(1L, 1L);
    }

    @Test
    @DisplayName("getClassroomById(): not found returns 404")
    void getClassroomById_notFound_returns404() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        when(this.classroomService.getClassroomById(eq(99L), anyLong()))
            .thenThrow(new ResourceNotFoundException("Classroom", "Not found"));

        this.mockMvc.perform(get("/api/v1/classrooms/99")
                .with(user(userDetails)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("createClassroom(): teacher creates successfully")
    void createClassroom_asTeacher_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);
        Classroom classroom = createTestClassroom(teacher);

        ClassroomCreateRequest request = new ClassroomCreateRequest("Test Class", "Description", "Note", null, 10L);

        when(this.classroomService.createClassroom(eq(1L), any(ClassroomCreateRequest.class))).thenReturn(classroom);

        this.mockMvc.perform(post("/api/v1/classrooms")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Classroom created successfully."))
            .andExpect(jsonPath("$.data.title").value("Test Class"));

        verify(this.classroomService, times(1)).createClassroom(eq(1L), any(ClassroomCreateRequest.class));
    }

    @Test
    @DisplayName("createClassroom(): student role forbidden")
    void createClassroom_asStudent_forbidden() throws Exception {
        User student = createStudent();
        CustomUserDetails userDetails = new CustomUserDetails(student);

        ClassroomCreateRequest request = new ClassroomCreateRequest("Test Class", "Desc", "Note", null, 10L);

        this.mockMvc.perform(post("/api/v1/classrooms")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());

        verify(this.classroomService, never()).createClassroom(anyLong(), any());
    }

    @Test
    @DisplayName("createClassroom(): blank title returns bad request")
    void createClassroom_blankTitle_badRequest() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        ClassroomCreateRequest request = new ClassroomCreateRequest("", "Desc", "Note", null, 10L);

        this.mockMvc.perform(post("/api/v1/classrooms")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("updateClassroom(): owner updates successfully")
    void updateClassroom_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        Subject subject = new Subject("math", "Mathematics", "en");
        subject.setSubjectId(10L);
        Classroom updated = new Classroom("Updated Title", "New Desc", "New Note", "ABC123", "en", teacher, subject);
        updated.setClassroomId(1L);
        updated.addUser(teacher);

        ClassroomUpdateRequest request = new ClassroomUpdateRequest("Updated Title", "New Desc", "New Note");

        when(this.classroomService.updateClassroom(eq(1L), eq(1L), any(ClassroomUpdateRequest.class))).thenReturn(updated);

        this.mockMvc.perform(put("/api/v1/classrooms/1")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Classroom updated successfully."))
            .andExpect(jsonPath("$.data.title").value("Updated Title"));
    }

    @Test
    @DisplayName("updateClassroom(): not owner returns bad request")
    void updateClassroom_notOwner_badRequest() throws Exception {
        User student = createStudent();
        CustomUserDetails userDetails = new CustomUserDetails(student);

        ClassroomUpdateRequest request = new ClassroomUpdateRequest("Title", "Desc", "Note");

        when(this.classroomService.updateClassroom(eq(2L), eq(1L), any(ClassroomUpdateRequest.class)))
            .thenThrow(new IllegalArgumentException("You are not allowed to update this classroom."));

        this.mockMvc.perform(put("/api/v1/classrooms/1")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("joinByCode(): joins classroom successfully")
    void joinByCode_success() throws Exception {
        User student = createStudent();
        CustomUserDetails userDetails = new CustomUserDetails(student);

        User teacher = createTeacher();
        Classroom classroom = createTestClassroom(teacher);
        classroom.addUser(student);

        when(this.classroomService.joinByCode(2L, "ABC123")).thenReturn(classroom);

        this.mockMvc.perform(post("/api/v1/classrooms/join")
                .with(user(userDetails))
                .param("code", "ABC123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Joined classroom successfully."));
    }

    @Test
    @DisplayName("joinByCode(): invalid code returns 404")
    void joinByCode_invalidCode_returns404() throws Exception {
        User student = createStudent();
        CustomUserDetails userDetails = new CustomUserDetails(student);

        when(this.classroomService.joinByCode(2L, "INVALID"))
            .thenThrow(new ResourceNotFoundException("Classroom", "Not found"));

        this.mockMvc.perform(post("/api/v1/classrooms/join")
                .with(user(userDetails))
                .param("code", "INVALID"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("leaveClassroom(): leaves successfully")
    void leaveClassroom_success() throws Exception {
        User student = createStudent();
        CustomUserDetails userDetails = new CustomUserDetails(student);

        doNothing().when(this.classroomService).leaveClassroom(2L, 1L);

        this.mockMvc.perform(post("/api/v1/classrooms/1/leave")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Left classroom successfully."));

        verify(this.classroomService, times(1)).leaveClassroom(2L, 1L);
    }

    @Test
    @DisplayName("leaveClassroom(): owner cannot leave returns bad request")
    void leaveClassroom_ownerCannotLeave_badRequest() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        doThrow(new IllegalArgumentException("Owner cannot leave their own classroom."))
            .when(this.classroomService).leaveClassroom(1L, 1L);

        this.mockMvc.perform(post("/api/v1/classrooms/1/leave")
                .with(user(userDetails)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("removeUserFromClassroom(): owner removes user successfully")
    void removeUserFromClassroom_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);
        Classroom classroom = createTestClassroom(teacher);

        when(this.classroomService.removeUserFromClassroom(1L, 1L, 2L)).thenReturn(classroom);

        this.mockMvc.perform(delete("/api/v1/classrooms/1/users/2")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("User removed from classroom successfully."));
    }

    @Test
    @DisplayName("addLearningMaterial(): owner adds successfully")
    void addLearningMaterial_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);
        Classroom classroom = createTestClassroom(teacher);
        LearningMaterial material = new LearningMaterial("Material", "Content", classroom, teacher);
        classroom.addLearningMaterial(material);

        LearningMaterialCreationRequest request = new LearningMaterialCreationRequest("Material", "Content");

        when(this.classroomService.addLearningMaterial(eq(1L), eq(1L), any(LearningMaterialCreationRequest.class)))
            .thenReturn(classroom);

        this.mockMvc.perform(post("/api/v1/classrooms/1/learning-materials")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Learning material added successfully."));
    }

    @Test
    @DisplayName("removeLearningMaterial(): owner removes successfully")
    void removeLearningMaterial_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);
        Classroom classroom = createTestClassroom(teacher);

        when(this.classroomService.removeLearningMaterial(1L, 1L, 5L)).thenReturn(classroom);

        this.mockMvc.perform(delete("/api/v1/classrooms/1/learning-materials/5")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Learning material removed successfully."));
    }

    @Test
    @DisplayName("addQuizToClassroom(): owner adds quiz successfully")
    void addQuizToClassroom_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);
        Classroom classroom = createTestClassroom(teacher);

        when(this.classroomService.addQuizToClassroom(1L, 1L, 10L)).thenReturn(classroom);

        this.mockMvc.perform(post("/api/v1/classrooms/1/quizzes/10")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Quiz added to classroom successfully."));
    }

    @Test
    @DisplayName("removeQuizFromClassroom(): owner removes quiz successfully")
    void removeQuizFromClassroom_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);
        Classroom classroom = createTestClassroom(teacher);

        when(this.classroomService.removeQuizFromClassroom(1L, 1L, 10L)).thenReturn(classroom);

        this.mockMvc.perform(delete("/api/v1/classrooms/1/quizzes/10")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Quiz removed from classroom successfully."));
    }
}

