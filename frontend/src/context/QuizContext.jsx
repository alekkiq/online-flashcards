import { createContext, useState, useEffect, useCallback } from "react";
import { getQuiz, getQuizHistory, saveQuizResult, getMyQuizzes } from "/src/api";
import { useAuth } from "../hooks/useAuth";

const QuizContext = createContext(null);

const QuizProvider = ({ children }) => {
  const { user } = useAuth();
  const [currentQuiz, setCurrentQuiz] = useState(null);
  const [quizHistory, setQuizHistory] = useState([]);
  const [userQuizzes, setUserQuizzes] = useState([]);
  const [userQuizzesLoading, setUserQuizzesLoading] = useState(false);
  const [userQuizzesFetched, setUserQuizzesFetched] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // game state
  const [currentCardIndex, setCurrentCardIndex] = useState(0);
  const [score, setScore] = useState(0);
  const [answeredCards, setAnsweredCards] = useState([]);
  const [isAnswered, setIsAnswered] = useState(false);

  const progress = currentQuiz
    ? (answeredCards.length / currentQuiz.flashcards.length) * 100
    : 0;

  /**
   * fetch a specific quiz by ID
   * @param {number} quizId the ID of the quiz to fetch
   */
  const fetchQuiz = useCallback(async (quizId) => {
    setLoading(true);
    setError(null);
    try {
      const [quizData, quizHistoryData] = await Promise.all([
        getQuiz(quizId),
        user && getQuizHistory(quizId),
      ]);

      if (!quizData.success) {
        return null;
      }

      setCurrentQuiz(quizData.data.data);
      setQuizHistory(quizHistoryData?.data?.data || []);
      return quizData;
    } catch (err) {
      setError(err.message);
      console.error("Error fetching quiz:", err);
      return null;
    } finally {
      setLoading(false);
    }
  }, [user]);

  /**
   * fetch user's quizzes
   */
  const fetchUserQuizzes = useCallback(async () => {
    if ((userQuizzesFetched || userQuizzesLoading) || !user) return;
    setUserQuizzesLoading(true);
    setError(null);
    try {
      const response = await getMyQuizzes();
      if (response.success) {
        setUserQuizzes(response.data.data);
        setUserQuizzesFetched(true);
      } else {
        setError(response.error);
      }
    } catch (err) {
      setError(err.message);
      console.error("Error fetching user quizzes:", err);
    } finally {
      setUserQuizzesLoading(false);
    }
  }, [userQuizzesFetched, userQuizzesLoading, user]);

  /**
   * save quiz attempt
   * @param {number} quizId The ID of the quiz to save attempt for
   * @param {number} score The score of the quiz attempt
   */
  const saveQuizAttempt = useCallback(async (quizId, score) => {
    try {
      const totalCards = currentQuiz?.flashcards?.length || 0;
      const scorePercentage = totalCards > 0 ? Math.round((score / totalCards) * 100) : 0;

      const response = await saveQuizResult(quizId, scorePercentage);
      if (!response.success) {
        return { success: false, error: response.error };
      }

      setQuizHistory((prev) => [...prev, response.data.data]);
      return { success: true };
    } catch (err) {
      console.error("Error saving quiz attempt:", err);
      return { success: false, error: err.message };
    }
  }, [currentQuiz]);

  /**
   * get current card
   * @returns {object|null} The current card
   */
  const getCurrentCard = useCallback(() => {
    if (!currentQuiz) return null;
    return currentQuiz.flashcards[currentCardIndex];
  }, [currentQuiz, currentCardIndex]);

  /**
   * get next card
   * @returns {object|null} The next card
   */
  const getNextCard = useCallback(() => {
    if (!currentQuiz) return null;
    if (currentCardIndex === currentQuiz.flashcards.length - 1) return null;
    const nextIndex = currentCardIndex + 1;
    setCurrentCardIndex(nextIndex);
    setIsAnswered(answeredCards.includes(nextIndex));
    return currentQuiz.flashcards[nextIndex];
  }, [currentQuiz, currentCardIndex, answeredCards]);

  const isQuizFinished = () => {
    if (!currentQuiz) return false;
    const isFinished = answeredCards.length === currentQuiz.flashcards.length;
    return isFinished;
  };

  /**
   * get previous card
   * @returns {object|null} The previous card
   */
  const getPreviousCard = useCallback(() => {
    if (!currentQuiz) return null;
    if (currentCardIndex === 0) return null;
    const prevIndex = currentCardIndex - 1;
    setCurrentCardIndex(prevIndex);
    setIsAnswered(answeredCards.includes(prevIndex));
    return currentQuiz.flashcards[prevIndex];
  }, [currentQuiz, currentCardIndex, answeredCards]);

  const advanceProgress = useCallback((isCorrect) => {
    if (isAnswered) return;
    if (isCorrect) {
      setScore((prev) => prev + 1);
    }
    setAnsweredCards((prev) => [...prev, currentCardIndex]);
    setIsAnswered(true);
  }, [isAnswered, currentCardIndex]);

  /**
   * clear quiz data
   */
  const clearQuiz = useCallback(() => {
    setCurrentQuiz(null);
    setQuizHistory([]);
    setError(null);
  }, []);

  const clearAllData = useCallback(() => {
    clearQuiz();
    resetGameState();
    setUserQuizzes([]);
    setUserQuizzesFetched(false);
    setUserQuizzesLoading(false);
  }, [user]);

  /**
   * reset game state (call when leaving quiz page)
   */
  const resetGameState = useCallback(() => {
    setCurrentCardIndex(0);
    setScore(0);
    setAnsweredCards([]);
    setIsAnswered(false);
  }, []);

  return (
    <QuizContext.Provider
      value={{
        currentQuiz,
        quizHistory,
        loading,
        error,
        fetchQuiz,
        saveQuizAttempt,
        clearQuiz,
        setCurrentQuiz,
        clearAllData,
        // game state
        currentCardIndex,
        score,
        progress,
        getCurrentCard,
        getNextCard,
        getPreviousCard,
        advanceProgress,
        isAnswered,
        setIsAnswered,
        resetGameState,
        isQuizFinished,
        userQuizzes,
        userQuizzesLoading,
        fetchUserQuizzes,
        setUserQuizzes,
      }}
    >
      {children}
    </QuizContext.Provider>
  );
};

export { QuizProvider, QuizContext };
