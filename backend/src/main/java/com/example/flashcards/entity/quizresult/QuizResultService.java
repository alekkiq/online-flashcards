package com.example.flashcards.entity.quizresult;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.entity.quiz.Quiz;
import com.example.flashcards.entity.quiz.QuizRepository;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;

@Service
public class QuizResultService implements IQuizResultService {

    private final QuizResultRepository quizResultRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public QuizResultService(QuizResultRepository quizResultRepository, UserRepository userRepository, QuizRepository quizRepository) {
        this.quizResultRepository = quizResultRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
    }

    private static final String USER_RESOURCE = "User";
    private static final String QUIZ_RESOURCE = "Quiz";
    private static final String USER_NOT_FOUND_KEY = "error.user.notFound";
    private static final String QUIZ_NOT_FOUND_KEY = "error.quiz.notFound";

    @Override
    @Transactional
    public QuizResult createQuizResult(long userId, long quizId, double scorePercentage) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_RESOURCE, userId, USER_NOT_FOUND_KEY, new Object[]{userId}));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException(QUIZ_RESOURCE, quizId, QUIZ_NOT_FOUND_KEY, new Object[]{quizId}));

        QuizResult result = new QuizResult(scorePercentage, null, user, quiz);
        return quizResultRepository.save(result);
    }

    @Override
    public List<QuizResult> getQuizResultByQuizAndUser(Long userId, Long quizId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_RESOURCE, userId, USER_NOT_FOUND_KEY, new Object[]{userId}));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException(QUIZ_RESOURCE, quizId, QUIZ_NOT_FOUND_KEY, new Object[]{quizId}));

        return quizResultRepository.findByQuizAndUser(quiz, user);
    }

    @Override
    public List<QuizResult> getQuizResultByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_RESOURCE, userId, USER_NOT_FOUND_KEY, new Object[]{userId}));
        return quizResultRepository.findByUser(user);
    }
}
