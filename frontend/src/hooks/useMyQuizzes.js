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
      subjectCode: quizData.subjectCode,
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
      subjectCode: quizData.subjectCode,
    };

    const response = await updateQuiz(id, data);
    if (!response.success) {
      setError(response.error);
      return;
    }

    const updated = response.data.data;
    const numericId = Number(id);

    setUserQuizzes((prev) =>
        prev.map((quiz) => (quiz.quizId === numericId ? updated : quiz))
    );

    return updated;
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
