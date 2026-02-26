package com.example.flashcards.entity.quiz;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.entity.flashcard.Flashcard;
import com.example.flashcards.entity.flashcard.dto.FlashcardResponse;
import com.example.flashcards.entity.quiz.dto.QuizCreationRequest;
import com.example.flashcards.entity.quiz.dto.QuizResponse;
import com.example.flashcards.entity.quiz.dto.QuizSeachResponse;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.subject.SubjectRepository;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;

@Service
public class QuizService implements IQuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, SubjectRepository subjectRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public QuizResponse getQuizById(long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz", "Quiz with ID " + id + " not found."));
        return mapToQuizResponse(quiz);
    }

    @Override
    public List<QuizSeachResponse> searchQuizzes() {
        return quizRepository.getAllQuizzes().stream()
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
    }

    @Override
    @Transactional
    public QuizResponse createQuiz(long userId, QuizCreationRequest request) {
        User creator = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "User with ID " + userId + " not found."));

        Subject subject = subjectRepository.findByName(request.subject())
            .orElseThrow(() -> new ResourceNotFoundException("Subject", "Subject with name '" + request.subject() + "' not found."));

        Quiz quiz = new Quiz(request.title(), request.description(), creator, subject);

        if (request.flashcards() != null) {
            request.flashcards().forEach(fc -> {
                Flashcard flashcard = new Flashcard(fc.question(), fc.answer(), quiz);
                quiz.addFlashcard(flashcard);
            });
        }

        Quiz savedQuiz = quizRepository.save(quiz);
        return mapToQuizResponse(savedQuiz);
    }

    @Override
    @Transactional
    public QuizResponse updateQuiz(long id, long userId, QuizCreationRequest request) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz", "Quiz with ID " + id + " not found."));

        Subject subject = subjectRepository.findByName(request.subject())
            .orElseThrow(() -> new ResourceNotFoundException("Subject", "Subject with name '" + request.subject() + "' not found."));

        quiz.setTitle(request.title());
        quiz.setDescription(request.description());
        quiz.setSubject(subject);

        quiz.getFlashcards().clear();
        if (request.flashcards() != null) {
            request.flashcards().forEach(fc -> {
                Flashcard flashcard = new Flashcard(fc.question(), fc.answer(), quiz);
                quiz.addFlashcard(flashcard);
            });
        }

        Quiz savedQuiz = quizRepository.save(quiz);
        return mapToQuizResponse(savedQuiz);
    }

    @Override
    @Transactional
    public void deleteQuiz(long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz", "Quiz with ID " + id + " not found.");
        }
        quizRepository.deleteById(id);
    }

    @Override
    public List<QuizResponse> getQuizzesByUser(long userId) {
        return quizRepository.findByCreator_UserId(userId).stream()
            .map(this::mapToQuizResponse)
            .toList();
    }

    private QuizResponse mapToQuizResponse(Quiz quiz) {
        List<FlashcardResponse> cardResponses = quiz.getFlashcards().stream()
            .map(card -> new FlashcardResponse(card.getFlashcardId(), card.getQuestion(), card.getAnswer()))
            .toList();

        return new QuizResponse(
            quiz.getQuizId(),
            quiz.getTitle(),
            quiz.getDescription(),
            quiz.getCreator().getUsername(), 
            quiz.getCreator().getRole().name(),
            quiz.getSubject().getName(), 
            quiz.getFlashcards().size(),
            cardResponses
        );
    }

}
