import { useEffect, useCallback } from "react";
import { useQuizContext } from "/src/hooks/useQuizContext";
import { createQuiz, updateQuiz } from "../api/quizApi";

export function useMyQuizzes() {
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
      subject: quizData.subject,
    };

    const response = await createQuiz(data);
    if (!response.success) {
      setError(response.error);
      return null;
    }
    const created = response.data.data;
    setUserQuizzes([...userQuizzes, created]);
    return created;
  };

  const handleUpdateQuiz = async (id, quizData) => {
    const data = {
      title: quizData.title,
      description: quizData.description,
      flashcards: quizData.cards,
      subject: quizData.subject,
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
