import { useEffect, useCallback } from "react";
import { useAuth } from "/src/hooks/useAuth";
import { useQuizContext } from "/src/hooks/useQuizContext";
import { createQuiz, updateQuiz } from "../api/quizApi";

export function useMyQuizzes() {
  const { user } = useAuth();
  const {
    userQuizzes,
    userQuizzesLoading: isLoading,
    fetchUserQuizzes,
    setUserQuizzes,
    error,
    setError,
  } = useQuizContext();

  const handleCreateQuiz = async (quizData) => {
    const data = {
      title: quizData.title,
      description: quizData.description,
      flashcards: quizData.cards,
      userId: user.id,
      subject: "Math", // TODO: Implement subject selection
    };

    const response = await createQuiz(data);
    if (!response.success) {
      setError(response.error);
      return;
    }
    setUserQuizzes([...userQuizzes, response.data.data]);
  };

  const handleUpdateQuiz = async (id, quizData) => {
    const data = {
      title: quizData.title,
      description: quizData.description,
      flashcards: quizData.cards,
      userId: user.id,
      subject: "Math", // TODO: Implement subject selection
    };

    const response = await updateQuiz(id, data);
    if (!response.success) {
      setError(response.error);
      return;
    }
    setUserQuizzes(userQuizzes.map((quiz) => (quiz.quizId === id ? response.data.data : quiz)));
  };

  return {
    quizzes: userQuizzes,
    isLoading,
    error,
    handleCreateQuiz,
    handleUpdateQuiz,
    fetchUserQuizzes,
  };
}
