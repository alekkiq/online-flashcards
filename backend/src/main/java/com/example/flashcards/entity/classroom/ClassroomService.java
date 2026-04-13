package com.example.flashcards.entity.classroom;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ForbiddenException;
import com.example.flashcards.common.exception.InvalidRequestException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.common.provider.CurrentLanguageProvider;
import com.example.flashcards.entity.classroom.dto.ClassroomCreateRequest;
import com.example.flashcards.entity.classroom.dto.ClassroomUpdateRequest;
import com.example.flashcards.entity.learningmaterial.LearningMaterial;
import com.example.flashcards.entity.learningmaterial.dto.LearningMaterialCreationRequest;
import com.example.flashcards.entity.quiz.Quiz;
import com.example.flashcards.entity.quiz.QuizRepository;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.subject.SubjectRepository;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class ClassroomService implements IClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final QuizRepository quizRepository;
    private final CurrentLanguageProvider currentLanguageProvider;

    private final SecureRandom random = new SecureRandom();
    private static final String CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public ClassroomService(
            ClassroomRepository classroomRepository,
            UserRepository userRepository,
            SubjectRepository subjectRepository,
            QuizRepository quizRepository,
            CurrentLanguageProvider currentLanguageProvider
    ) {
        this.classroomRepository = classroomRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.quizRepository = quizRepository;
        this.currentLanguageProvider = currentLanguageProvider;
    }

    @Override
    @Transactional(readOnly = true)
    public Classroom getClassroomById(Long classroomId, Long userId) {
        return this.classroomRepository.findByClassroomIdAndUsers_UserId(classroomId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classroom",
                        classroomId,
                        "error.classroom.notMember",
                        new Object[]{classroomId}
                ));
    }

    private Classroom getClassroomEntityById(Long classroomId) {
        return this.classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classroom",
                        classroomId,
                        "error.classroom.notFound",
                        new Object[]{classroomId}
                ));
    }

    private User getUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        userId,
                        "error.user.notFound",
                        new Object[]{userId}
                ));
    }

    private Subject getSubjectById(Long subjectId) {
        return this.subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject",
                        subjectId,
                        "error.subject.notFound",
                        new Object[]{subjectId}
                ));
    }

    private String randomCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
        }
        return sb.toString();
    }

    private String generateUniqueJoinCode(int length) {
        for (int attempt = 0; attempt < 10; attempt++) {
            String code = randomCode(length);
            if (!classroomRepository.existsByJoinCode(code)) {
                return code;
            }
        }
        throw new InvalidRequestException("error.classroom.joinCode.generate", null);
    }

    private String normalizeJoinCode(String input) {
        if (input == null) return null;
        String trimmed = input.trim();
        if (trimmed.isEmpty()) return null;
        return trimmed.toUpperCase(Locale.ROOT);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Classroom> getMyClassrooms(Long userId) {
        return this.classroomRepository.findByUsers_UserId(userId);
    }

    @Override
    @Transactional
    public Classroom createClassroom(Long userId, ClassroomCreateRequest request) {
        User owner = getUserById(userId);
        String requestedCode = normalizeJoinCode(request.joinCode());
        String joinCode;

        String language = currentLanguageProvider.getCurrentLanguage();
        Subject subject = getSubjectById(request.subjectId());

        if (!subject.getLanguage().equals(language)) {
            throw new InvalidRequestException("error.classroom.subject.languageMismatch", null);
        }

        if (requestedCode == null) {
            joinCode = generateUniqueJoinCode(6);
        } else {
            if (requestedCode.length() < 6) {
                throw new InvalidRequestException("error.classroom.joinCode.tooShort", null);
            }
            if (classroomRepository.existsByJoinCode(requestedCode)) {
                throw new DuplicateResourceException("Classroom", "error.classroom.joinCode.inUse", null);
            }
            joinCode = requestedCode;
        }

        Classroom classroom = new Classroom(
                request.title(),
                request.description(),
                request.note(),
                joinCode,
                language,
                owner,
                subject
        );

        classroom.addUser(owner);
        return this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom updateClassroom(Long userId, Long classroomId, ClassroomUpdateRequest request) {
        Classroom classroom = getClassroomEntityById(classroomId);

        if (classroom.getOwner() == null || !Objects.equals(classroom.getOwner().getUserId(), userId)) {
            throw new ForbiddenException("error.classroom.notOwner", null);
        }

        classroom.setTitle(request.title());
        classroom.setDescription(request.description());
        classroom.setNote(request.note());

        return this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom joinByCode(Long userId, String joinCode) {
        User user = getUserById(userId);

        Classroom classroom = this.classroomRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classroom",
                        "error.classroom.joinCode.notFound",
                        new Object[]{joinCode}
                ));

        boolean alreadyMember = classroom.getUsers().stream()
                .anyMatch(u -> Objects.equals(u.getUserId(), userId));

        if (!alreadyMember) {
            classroom.addUser(user);
            return this.classroomRepository.save(classroom);
        }

        return classroom;
    }

    @Override
    @Transactional
    public void leaveClassroom(Long userId, Long classroomId) {
        Classroom classroom = getClassroomEntityById(classroomId);

        if (classroom.getOwner() != null && Objects.equals(classroom.getOwner().getUserId(), userId)) {
            throw new InvalidRequestException("error.classroom.leave.owner", null);
        }

        classroom.getUsers().removeIf(u -> Objects.equals(u.getUserId(), userId));
        this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom removeUserFromClassroom(Long ownerId, Long classroomId, Long targetUserId) {
        Classroom classroom = getClassroomEntityById(classroomId);

        if (classroom.getOwner() == null || !Objects.equals(classroom.getOwner().getUserId(), ownerId)) {
            throw new ForbiddenException("error.classroom.removeUser.notOwner", null);
        }

        if (Objects.equals(ownerId, targetUserId)) {
            throw new InvalidRequestException("error.classroom.removeUser.self", null);
        }

        classroom.getUsers().removeIf(u -> Objects.equals(u.getUserId(), targetUserId));
        return this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom addLearningMaterial(Long userId, Long classroomId, LearningMaterialCreationRequest request) {
        User creator = getUserById(userId);
        Classroom classroom = getClassroomEntityById(classroomId);

        if (classroom.getOwner() == null || !Objects.equals(classroom.getOwner().getUserId(), userId)) {
            throw new ForbiddenException("error.classroom.material.add.notOwner", null);
        }

        LearningMaterial learningMaterial = new LearningMaterial(
                request.title(),
                request.content(),
                classroom,
                creator
        );

        classroom.addLearningMaterial(learningMaterial);
        return this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom removeLearningMaterial(Long userId, Long classroomId, Long learningMaterialId) {
        Classroom classroom = getClassroomEntityById(classroomId);

        if (classroom.getOwner() == null || !Objects.equals(classroom.getOwner().getUserId(), userId)) {
            throw new ForbiddenException("error.classroom.material.remove.notOwner", null);
        }

        classroom.getLearningMaterials().removeIf(lm -> Objects.equals(lm.getLearningMaterialId(), learningMaterialId));
        return this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom addQuizToClassroom(Long userId, Long classroomId, Long quizId) {
        Classroom classroom = getClassroomEntityById(classroomId);

        if (classroom.getOwner() == null || !Objects.equals(classroom.getOwner().getUserId(), userId)) {
            throw new ForbiddenException("error.classroom.quiz.add.notOwner", null);
        }

        boolean alreadyAdded = classroom.getQuizzes().stream()
                .anyMatch(q -> q.getQuizId() == quizId);

        if (alreadyAdded) {
            return classroom;
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Quiz",
                        quizId,
                        "error.quiz.notFound",
                        new Object[]{quizId}
                ));

        classroom.addQuiz(quiz);
        return this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom removeQuizFromClassroom(Long userId, Long classroomId, Long quizId) {
        Classroom classroom = getClassroomEntityById(classroomId);

        if (classroom.getOwner() == null || !Objects.equals(classroom.getOwner().getUserId(), userId)) {
            throw new ForbiddenException("error.classroom.quiz.remove.notOwner", null);
        }

        classroom.getQuizzes().removeIf(q -> q.getQuizId() == quizId);
        return this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom editLearningMaterial(Long userId, Long classroomId, Long learningMaterialId, LearningMaterialCreationRequest request) {
        Classroom classroom = getClassroomEntityById(classroomId);

        if (classroom.getOwner() == null || !Objects.equals(classroom.getOwner().getUserId(), userId)) {
            throw new ForbiddenException("error.classroom.material.edit.notOwner", null);
        }

        LearningMaterial learningMaterial = classroom.getLearningMaterials().stream()
                .filter(lm -> Objects.equals(lm.getLearningMaterialId(), learningMaterialId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "LearningMaterial",
                        learningMaterialId,
                        "error.classroom.material.notFound",
                        new Object[]{learningMaterialId}
                ));

        learningMaterial.setTitle(request.title());
        learningMaterial.setContent(request.content());

        return this.classroomRepository.save(classroom);
    }
}