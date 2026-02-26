package com.example.flashcards.entity.classroom;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.entity.classroom.dto.ClassroomCreateRequest;
import com.example.flashcards.entity.classroom.dto.ClassroomUpdateRequest;
import com.example.flashcards.entity.learningmaterial.LearningMaterial;
import com.example.flashcards.entity.learningmaterial.dto.LearningMaterialCreationRequest;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.subject.SubjectRepository;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
public class ClassroomService implements IClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    private final SecureRandom random = new SecureRandom();
    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; 

    public ClassroomService(
            ClassroomRepository classroomRepository,
            UserRepository userRepository,
            SubjectRepository subjectRepository
    ) {
        this.classroomRepository = classroomRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }


    @Override
    public Classroom getClassroomById(Long classroomId) {
        return this.classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classroom",
                        classroomId,
                        "Classroom with ID " + classroomId + " not found"
                ));
    }


    private User getUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        userId,
                        "User with ID " + userId + " not found"
                ));
    }

    private Subject getSubjectById(Long subjectId) {
        return this.subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject",
                        subjectId,
                        "Subject with ID " + subjectId + " not found"
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
        throw new IllegalStateException("Could not generate unique join code.");
    }


    @Override
    public List<Classroom> getMyClassrooms(Long userId) {
        return this.classroomRepository.findByUsers_UserId(userId);
    }

    @Override
    @Transactional
    public Classroom createClassroom(Long userId, ClassroomCreateRequest request) {
        User owner = getUserById(userId);
        Subject subject = getSubjectById(request.subjectId());

        String joinCode = generateUniqueJoinCode(6);

        Classroom classroom = new Classroom(
                request.title(),
                request.description(),
                request.note(),
                joinCode,
                owner,
                subject
        );

        classroom.addUser(owner);

        return this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom updateClassroom(Long userId, Long classroomId, ClassroomUpdateRequest request) {
        Classroom classroom = getClassroomById(classroomId);

        if (classroom.getOwner() == null || classroom.getOwner().getUserId() != userId) {
            throw new IllegalArgumentException("You are not allowed to update this classroom.");
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
                        "Classroom with join code " + joinCode + " not found"
                ));

        boolean alreadyMember = classroom.getUsers().stream()
                .anyMatch(u -> u.getUserId() == userId);

        if (!alreadyMember) {
            classroom.addUser(user);
            return this.classroomRepository.save(classroom);
        }

        return classroom;
    }

    @Override
    @Transactional
    public void leaveClassroom(Long userId, Long classroomId) {
        Classroom classroom = getClassroomById(classroomId);

        if (classroom.getOwner() != null && classroom.getOwner().getUserId() == userId) {
            throw new IllegalArgumentException("Owner cannot leave their own classroom.");
        }

        classroom.getUsers().removeIf(u -> u.getUserId() == userId);
        this.classroomRepository.save(classroom);
    }

    @Override
    @Transactional
    public Classroom addLearningMaterial(Long userId, Long classroomId, LearningMaterialCreationRequest request) {
        User creator = getUserById(userId);
        Classroom classroom = getClassroomById(classroomId);

        if (classroom.getOwner() == null || classroom.getOwner().getUserId() != userId) {
            throw new IllegalArgumentException("You are not allowed to add learning material to this classroom.");
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
        Classroom classroom = getClassroomById(classroomId);

        if (classroom.getOwner() == null || classroom.getOwner().getUserId() != userId) {
            throw new IllegalArgumentException("You are not allowed to remove learning material from this classroom.");
        }

        classroom.getLearningMaterials().removeIf(lm -> lm.getLearningMaterialId() == learningMaterialId);
        return this.classroomRepository.save(classroom);
    }
}