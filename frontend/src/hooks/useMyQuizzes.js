import { useEffect, useState } from "react";
import { useAuth } from "/src/hooks/useAuth";

export function useMyQuizzes() {
  const [quizzes, setQuizzes] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useAuth();

  useEffect(() => {
    const fetchQuizzes = async () => {
      setIsLoading(true);
      try {
        setTimeout(() => {
          setQuizzes([
            {
              id: 1,
              title: "Quiz 1",
              description: "Description 1",
              flashcards: [
                {
                  id: 1,
                  front: "Front 1",
                  back: "Back 1",
                },
                {
                  id: 2,
                  front: "Front 2",
                  back: "Back 2",
                },
              ],
            },
          ]);
          setIsLoading(false);
        }, 500);
      } catch (err) {
        setError(err);
        setIsLoading(false);
      }
    };

    fetchQuizzes();
  }, []);

  const handleCreateQuiz = async (quizData) => {
    setIsLoading(true);
    const data = {
      title: quizData.title,
      description: quizData.description,
      flashcards: quizData.cards,
      userId: user.id,
    };

    // TODO: Implement quiz creation
  };

  const handleUpdateQuiz = async (id, quizData) => {
    setIsLoading(true);
    const data = {
      title: quizData.title,
      description: quizData.description,
      flashcards: quizData.cards,
      userId: user.id,
    };

    // TODO: Implement quiz update
  };

  return {
    quizzes,
    isLoading,
    error,
    handleCreateQuiz,
    handleUpdateQuiz,
  };
}
