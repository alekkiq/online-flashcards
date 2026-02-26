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

    @Override
    @Transactional
    public QuizResult createQuizResult(long userId, long quizId, double scorePercentage) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User with ID " + userId + " not found."));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz", "Quiz with ID " + quizId + " not found."));

        QuizResult result = new QuizResult(scorePercentage, null, user, quiz);
        return quizResultRepository.save(result);
    }

    @Override
    public List<QuizResult> getQuizResultByQuizAndUser(Long userId, Long quizId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User with ID " + userId + " not found."));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz", "Quiz with ID " + quizId + " not found."));

        return quizResultRepository.findByQuizAndUser(quiz, user);
    }

    @Override
    public List<QuizResult> getQuizResultByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User with ID " + userId + " not found."));
        return quizResultRepository.findByUser(user);
    }
}
